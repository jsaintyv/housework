import jQuery from "jquery"; 

import query from "./QueryUtils.js"; 
class UserService {
    
    constructor()
    {
        this.defUser = null;
    }
    
    currentUser() {
        if(this.defUser == null) {
            this.defUser = jQuery.get("/api/user/current"); 
        } 
        return this.defUser;
    }
    
    login(login, password) {            
        this.defUser = jQuery.post("/login", {
            login: login,
            password: password
        });
        
        return this.defUser;
    }
    
    register(login, password) {            
        this.defUser = jQuery.post("/register", {
            login: login,
            password: password
        });
        
        return this.defUser;
    }

    update(user, newPassword) {            
        return query.post("/api/user/update/" + user.id, {
            name: user.name,
            updatePassword: newPassword !== null,
            password: newPassword
        });
        
        
    }
}

var instance = new UserService();

export default instance;