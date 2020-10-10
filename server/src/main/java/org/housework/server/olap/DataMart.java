package org.housework.server.olap;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.efficientolap.OlapDimension;
import org.efficientolap.OlapDimensionId;
import org.efficientolap.OlapDimensionQuery;
import org.efficientolap.OlapDimensionYearWeek;
import org.efficientolap.OlapMeasureDouble;
import org.housework.server.models.Work;
import org.housework.server.models.WorkRepository;

public class DataMart {
	private static final String DIMENSION_USER_CODE = "USER";
	private static final String DIMENSION_TASK_TYPE_CODE = "TASK_TYPE";
	private static final String DIMENSION_TEMPS = "TEMPS";
	private static final String DIMENSION_WORK_ID = "WORK_ID";
	private static final String MEASURE_SCORING_CODE = "SCORING";
		
	public final List<OlapDimensionQuery> listDimensions;
	
	public final OlapDimensionQuery userDimension;
	public final OlapDimensionQuery typeDimension;
	public final OlapDimensionYearWeek weekDimension;
	public final OlapDimensionId workIdDimension;
	
	public final OlapMeasureDouble scoreMeasure;
	
		
	public DataMart() {
		listDimensions = new ArrayList<OlapDimensionQuery>();
		listDimensions.add(userDimension = new OlapDimensionQuery(DIMENSION_USER_CODE, 
				"SELECT " 
						+ " ID as " + OlapDimensionQuery.COL_ID + "," 
						+ " LOGIN as " + OlapDimensionQuery.COL_CODE 
						+ " FROM USERS")
		);		
		listDimensions.add(typeDimension = new OlapDimensionQuery(
				DIMENSION_TASK_TYPE_CODE, 
				"SELECT "
				+ " ID as " + OlapDimensionQuery.COL_ID + ","
				+ " NAME as " + OlapDimensionQuery.COL_CODE
				+ " FROM TASK_TYPE")
		);
		
		weekDimension = new OlapDimensionYearWeek(DIMENSION_TEMPS, 2019, 2030);		
		workIdDimension = new OlapDimensionId(DIMENSION_WORK_ID, (int)10e7);
		
		scoreMeasure = new OlapMeasureDouble(MEASURE_SCORING_CODE, new OlapDimension[]{ userDimension, typeDimension, weekDimension, workIdDimension})
					.defineConsumerMapping(userDimension, (o)->userDimension.getMember(((Work)o).getWorker().getId()))
					.defineConsumerMapping(typeDimension, (o)->typeDimension.getMember(((Work)o).getType().getId()))
					.defineConsumerMapping(weekDimension, (o)->weekDimension.getMember(((Work)o).getDate()))
					.defineConsumerMapping(workIdDimension, (o)->workIdDimension.getMember(((Work)o).getId()))
					.defineGetter((o)->((Work)o).getType().getValue());
		
	}
	
	public void update(Connection connection, WorkRepository repository) throws SQLException {
		for (OlapDimensionQuery dim : listDimensions) {
			dim.update(connection);
		}
		
		repository.findAll().forEach((w)->scoreMeasure.register(w));
		
	}
	
}
