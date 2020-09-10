import store from './stores'
import lang from './lang'

var initVue = (Vue) => {
    Vue.prototype.$lang = lang;
    Vue.prototype.$store = store;

    Vue.filter('displayName', function(user) {
        if (user.name) {    
          return user.name;        
        }
        return user.login;
      });
      
      Vue.filter('weekday', function(value) {
        if (value) {    
          return lang.days[value.getDay()]    
        }
      });
      
      Vue.filter('formatDate', function(value) {
        if (value) {    
          var dayAsStr= "" +value.getDate() ;
          if(dayAsStr.length == 1) {
            dayAsStr = "0" + dayAsStr;
          }
      
          var monthAsStr = "" + (value.getMonth()+1);
          if(monthAsStr.length == 1) {
            monthAsStr = "0" + monthAsStr;
          }
          return dayAsStr + "/" + monthAsStr + "/" + value.getFullYear();
        };
    });
}

export default initVue;