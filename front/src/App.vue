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
        <b-dropdown-divider></b-dropdown-divider>
        <MenuEntriesHouse />
        <b-dropdown-divider></b-dropdown-divider>
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
import TaskTypes from './components/TaskTypes.vue'
import WorkersList from './components/WorkersList'
import HouseInfo from './components/HouseInfo'
import LoginStatus from './components/LoginStatus.vue'
import UserHouses from './components/UserHouses.vue'
import MenuEntriesHouse from './components/MenuEntriesHouse.vue'
import {mapState} from 'vuex'
import {default as store, ACTION_SELECT_HOUSE_ID, ACTION_CHECK_CONNECTED, STATUS_DISCONNECTED} from './stores';

const needConnectedMap = {
  "houseBoard": true,
  "taskTypes": true,
  "workers": true,
  "houseInfo": true
};

const routes = [
    { path: '/', component: Login },
    { path: '/board', component: UserBoard },
    { path: '/houseBoard/:houseId', name:"houseBoard", component: HouseBoard },
    { path: '/taskTypes/:houseId', name:"taskTypes", component: TaskTypes },
    { path: '/workers/:houseId', name:"workers", component: WorkersList },
    { path: '/houseInfo/:houseId', name:"houseInfo", component: HouseInfo },
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
    'UserHouses': UserHouses,
    'MenuEntriesHouse': MenuEntriesHouse  
  },
  router,
  store,
  computed: mapState(['stateConnected']),
  watch: {
    stateConnected(newStat) {
      if(newStat == STATUS_DISCONNECTED && this.$route.matched.filter((p)=>!needConnectedMap[p.name]).length == 0) {
        // All path need to be connected. Reroute on login
        this.$router.push('/');
      } 
    },
    $route( to,  from ) {
      console.log(to, from);
      var houseId = this.$route.params.houseId;
      
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
