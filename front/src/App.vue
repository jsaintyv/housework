<template>
  <div id="app">    
  	<b-navbar id="bar" toggleable="lg" type="dark" variant="info">
    	<b-navbar-brand href="#">House Work </b-navbar-brand> 
    	 <b-navbar-nav class="ml-auto">
    	  	<b-nav-item-dropdown right>	
    	  		<template v-slot:button-content>
					<LoginStatus />
				</template>
				<b-dropdown-item href="#">Accueil</b-dropdown-item>
        <UserHouses />
        <b-dropdown-item href="#/">Déconnexion</b-dropdown-item>
			</b-nav-item-dropdown>
    	</b-navbar-nav>
    </b-navbar>
  	<div id="main">   	
      <router-view></router-view>
    </div>
  </div> 
</template> 

<script>

import VueRouter from 'vue-router'
import Login from './components/Login.vue'
import Register from './components/Register.vue'
import PostRegister from './components/PostRegister.vue'
import UserBoard from './components/UserBoard.vue'
import LoginStatus from './components/LoginStatus.vue'
import UserHouses from './components/UserHouses.vue'
import store from './stores';

console.log(store);

const routes = [
    { path: '/', component: Login },
    { path: '/board', component: UserBoard },
    { path: '/register', component: Register },
    { path: '/postRegister', component: PostRegister },
  ];
  
const router = new VueRouter({
    routes // short for `routes: routes`
});


export default {
  name: 'app',
  components: {
    'LoginStatus': LoginStatus,
    'UserHouses': UserHouses  
  },
  router,
  store
};
</script>

<style>
#app {
  
}

#main {
  position:absolute;
  top:70px;
  left:0px;
  right:0px;
}
</style>
