import query from "./QueryUtils.js"; 

class TaskTypeService {    
    constructor() {
    }
    
    /**
     * @return deffered
     */
    create(houseId, taskTypeForm) {
        return query.post("/api/taskType/create/" + houseId, taskTypeForm);      
    }
    
    
    /**
     * @return deffered
     */
    update(houseId, taskType) {
        return query.post("/api/taskType/update/" + houseId + "/" + taskType.id, taskType);      
    }
    
    /**
     * @return deffered
     */
    remove(houseId, taskType) {
        return query.delete("/api/taskType/delete/" + houseId + "/" + taskType.id);      
    }
}

var instance = new TaskTypeService();
export default instance;
