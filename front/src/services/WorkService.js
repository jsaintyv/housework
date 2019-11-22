import query from "./QueryUtils.js"; 

class WorkService {    
    constructor() {
    }
    
    /**
     * @return deffered
     */
    create(houseId, workForm) {
        return query.post("/api/work/create/" + houseId, workForm);      
    }

    /**
     * Get list of works
     * @param {*} houseId 
     * @param {*} startTime in millis from epoch
     * @param {*} endTime  in millis from epoch
     * @return deffered
     */
    list(houseId, startTime, endTime) {
        return query.get("/api/work/list/" + houseId + "/" + startTime + "/" + endTime);
    }

      
    /**
     * @return deffered
     */
    remove(houseId, work) {
        return query.delete("/api/work/remove/" + houseId + "/" + work.id);      
    }
}

var instance = new WorkService();
export default instance;
