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
import Register from './components/Register.vue'
import PostRegister from './components/PostRegister.vue'
import LoginStatus from './components/LoginStatus.vue'



const routes = [
    { path: '/', component: Login },
    { path: '/house', component: LoginStatus },
    { path: '/register', component: Register },
    { path: '/postRegister', component: PostRegister },
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
