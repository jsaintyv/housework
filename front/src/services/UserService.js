
import jQuery from "jquery"; 

class UserService {
    
    constructor()
    {
        this.defUser = null;
    }
    
    currentUser() {
        if(this.defUser == null) {
            this.defUser = jQuery.get("/api/user/connected"); 
        } 
        return defUser;
    }
    
    login(login, password) {            
        this.defUser = jQuery.post("/login", {
            login: login,
            password: password
        });
        
        return this.defUser;
    }    
};

var instance = new UserService();


export default instance;