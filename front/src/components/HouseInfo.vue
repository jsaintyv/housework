<template>
    
	<div v-if="getSelected != null">{{ getSelected.name }}  ({{ getSelected.id}})</div>
</template>

<script>

import {default as store} from "../stores/index.js";

export default {
  name: 'HouseInfo',
  computed: {
	  getSelected() {
		  return store.state.selectedHouse;
	  }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>