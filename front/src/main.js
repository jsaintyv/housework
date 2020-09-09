import '@babel/polyfill'
import 'material-design-icons/iconfont/material-icons.css'
import 'mutationobserver-shim'
import Vue from 'vue'
import VueRouter from 'vue-router'
import './plugins/bootstrap-vue'
import App from './App.vue'
import lang from './lang'


import store from './stores'

Vue.use(VueRouter);
Vue.config.productionTip = false;

Vue.prototype.$lang = lang;
Vue.prototype.$store = store;

window.houseWorkStore = store;


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
  }
});

new Vue({
  render: h => h(App)  
}).$mount('#app')

