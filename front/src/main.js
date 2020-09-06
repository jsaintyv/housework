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


Vue.filter('formatDate', function(value) {
  if (value) {
    return value.getDate() + "/" + value.getMonth() + "/" + value.getYear();
  }
});

new Vue({
  render: h => h(App)  
}).$mount('#app')

