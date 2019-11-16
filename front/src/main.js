import '@babel/polyfill'
import 'mutationobserver-shim'
import Vue from 'vue'
import VueRouter from 'vue-router'
import './plugins/bootstrap-vue'
import App from './App.vue'


import store from './stores'

Vue.use(VueRouter);
Vue.config.productionTip = false;

Vue.prototype.$store = store;

new Vue({
  render: h => h(App)  
}).$mount('#app')

