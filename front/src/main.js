import '@babel/polyfill'
import 'material-design-icons/iconfont/material-icons.css'
import 'mutationobserver-shim'
import Vue from 'vue'
import VueRouter from 'vue-router'
import './plugins/bootstrap-vue'
import App from './App.vue'
import initVue from './initVue'
import store from './stores'


Vue.use(VueRouter);
Vue.config.productionTip = false;


window.houseWorkStore = store;

initVue(Vue);

new Vue({
  render: h => h(App)  
}).$mount('#app')

