import jQuery from "jquery"; 

class HouseService {    
    constructor() {
    }
    
    /**
     * @return deffered
     */
    create(houseForm) {
        return jQuery.post("/api/house/create", houseForm);      
    }
    
    /**
     * @return deffered
     */
    retrieve(houseId) {
        return jQuery.get("/api/house/retrieve/" + houseId);      
    }
    
    /**
     * @return deffered
     */
    update(houseId, houseForm) {
        return jQuery.post("/api/house/update/" + houseId, {houeForm: houseForm});      
    }
    
    /**
     * @return deffered
     */
    remove(houseId) {
        return jQuery.ajax({url :"/api/house/delete/" + houseId, method: "DELETE"});      
    }
    
    /**
     * @return deffered
     */
    listOwned() {
        return jQuery.get("/api/house/listOwned");      
    }
    
    /**
     * @return deffered
     */
    listAvailables() {
        return jQuery.get("/api/house/listAvailables");      
    }
}

var instance = new HouseService();
export default instance;
