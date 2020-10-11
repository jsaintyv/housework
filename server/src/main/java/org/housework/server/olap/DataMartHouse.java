package org.housework.server.olap;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.efficientolap.OlapDimension;
import org.efficientolap.OlapDimensionId;
import org.efficientolap.OlapDimensionCollection;
import org.efficientolap.OlapDimensionYearWeek;
import org.efficientolap.OlapMeasureDouble;
import org.housework.server.models.House;
import org.housework.server.models.TaskType;
import org.housework.server.models.User;
import org.housework.server.models.Work;
import org.housework.server.models.WorkRepository;

/**
 * Manage MicroCube by house
 * @author jsy 
 */
public class DataMartHouse {
	private static final String DIMENSION_USER_CODE = "USER";
	private static final String DIMENSION_TASK_TYPE_CODE = "TASK_TYPE";
	private static final String DIMENSION_TEMPS = "TEMPS";
	private static final String DIMENSION_WORK_ID = "WORK_ID";
	private static final String MEASURE_SCORING_CODE = "SCORING";
		
	public final List<OlapDimension> listDimensions;
	
	public final int houseId; 
	
	public final OlapDimensionCollection<User> userDimension;
	public final OlapDimensionCollection<TaskType> typeDimension;
	public final OlapDimensionYearWeek weekDimension;
	public final OlapDimensionId workIdDimension;
	
	public final OlapMeasureDouble scoreMeasure;
	
	 
 	private static Map<Integer, WeakReference<DataMartHouse>> datamartsByHouseId = new HashMap<Integer, WeakReference<DataMartHouse>>();
	
			
	private DataMartHouse(int houseId) {
		this.houseId = houseId;
		
		listDimensions = new ArrayList<OlapDimension>();
		listDimensions.add(userDimension = new OlapDimensionCollection<>(DIMENSION_USER_CODE, User::getId, User::getNameOrLogin));				
		listDimensions.add(typeDimension = new OlapDimensionCollection<>(DIMENSION_TASK_TYPE_CODE, TaskType::getId, TaskType::getName));				
		weekDimension = new OlapDimensionYearWeek(DIMENSION_TEMPS, 2019, Calendar.getInstance().get(Calendar.YEAR) + 1);		
		workIdDimension = new OlapDimensionId(DIMENSION_WORK_ID, (int)10e7);
		
		scoreMeasure = new OlapMeasureDouble(MEASURE_SCORING_CODE, new OlapDimension[]{ userDimension, typeDimension, weekDimension, workIdDimension})
					.defineConsumerMapping(userDimension, (o)->userDimension.getMember(((Work)o).getWorker().getId()))
					.defineConsumerMapping(typeDimension, (o)->typeDimension.getMember(((Work)o).getType().getId()))
					.defineConsumerMapping(weekDimension, (o)->weekDimension.getMember(((Work)o).getDate()))
					.defineConsumerMapping(workIdDimension, (o)->workIdDimension.getMember(((Work)o).getId()))
					.defineGetter((o)->((Work)o).getType().getValue());
		
	}
	
	public void update(House house, WorkRepository repository) {
		userDimension.clear();
		userDimension.register(house.getUsers());
		
		typeDimension.clear();
		typeDimension.register(house.getTypes());
		
		repository.findBy(house).forEach((w)->scoreMeasure.register(w));		
	}
	
	public static DataMartHouse getDatamart(House house) {		
		synchronized (datamartsByHouseId) {
			WeakReference<DataMartHouse> ref = datamartsByHouseId.computeIfAbsent(house.getId(), (id)->new WeakReference<>(new DataMartHouse(house.getId())));
			DataMartHouse datamart = ref.get();
			if(datamart == null) {
				datamart = new DataMartHouse(house.getId());
				datamartsByHouseId.put(house.getId(), new WeakReference<DataMartHouse>(datamart));
			}		
			return datamart;
		}		
	}
}
