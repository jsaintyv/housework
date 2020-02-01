<template>
  <div id="app">    
  	<b-navbar id="bar" toggleable="lg" type="dark" variant="info">
    	<b-navbar-brand href="#">House Work</b-navbar-brand> 
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
import HouseBoard from './components/HouseBoard.vue'
import LoginStatus from './components/LoginStatus.vue'
import UserHouses from './components/UserHouses.vue'
import {mapState} from 'vuex'
import {default as store, ACTION_SELECT_HOUSE_ID, ACTION_CHECK_CONNECTED} from './stores';

const routes = [
    { path: '/', component: Login },
    { path: '/board', component: UserBoard },
    { path: '/houseBoard/:houseId', component: HouseBoard },
    { path: '/register', component: Register },
    { path: '/postRegister', component: PostRegister },
  ];
  
const router = new VueRouter({
    routes // short for `routes: routes`
});


export default {
  name: 'app',
  created() {
    store.dispatch(ACTION_CHECK_CONNECTED);

    console.log("Created:", this.$route.params);
    var houseId = this.$route.params.houseId;
    console.log(houseId);
    if(houseId) {
      store.dispatch(ACTION_SELECT_HOUSE_ID, houseId);
    }
  },
  components: {
    'LoginStatus': LoginStatus,
    'UserHouses': UserHouses  
  },
  router,
  store,
  computed: mapState(['stateConnected']),
  watch: {
    stateConnected(n, o) {
      console.log('stateConnected', n, o);
    },
    $route(to, from) {
      console.log(this);
      console.log(from);
      console.log(this.$route.params);
      var houseId = this.$route.params.houseId;
      console.log(houseId);
      if(houseId) {
        store.dispatch(ACTION_SELECT_HOUSE_ID, houseId);
      }
    }
  }
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
