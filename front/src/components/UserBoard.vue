<template>
	<UserHouses />
</template>

<script>
import UserHouses from "./UserHouses.vue";

export default {
  name: 'UserBoard',
  data(){
    var model = {
            
    };
    
	return model;
  },
  components: {
  	'UserHouses': UserHouses
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
