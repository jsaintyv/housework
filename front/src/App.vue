<template>
  <div id="app">
  	<b-navbar toggleable="lg" type="dark" variant="info">
    	<b-navbar-brand href="#">House Work</b-navbar-brand>
    </b-navbar>
  	   	
    <router-view></router-view>
  </div> 
</template> 

<script>

import VueRouter from 'vue-router'
import Login from './components/Login.vue'
import LoginStatus from './components/LoginStatus.vue'



const routes = [
    { path: '/', component: Login },
    { path: '/house', component: LoginStatus }
  ];
  
const router = new VueRouter({
    routes // short for `routes: routes`
});


export default {
  name: 'app',
  router
};
</script>

<style>
#app {
    
}
</style>
