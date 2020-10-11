<template>
	<div v-if="getSelected != null">
		<b-dropdown-item :to="{name: 'taskTypes', params: {houseId: getSelected.id}}">
			{{lang.TypesLabel}}	
		</b-dropdown-item>
		<b-dropdown-item :to="{name: 'workers', params: {houseId: getSelected.id}}">
			{{lang.WorkersLabel}}	
		</b-dropdown-item>
		<b-dropdown-item :to="{name: 'houseInfo', params: {houseId: getSelected.id}}">
			{{lang.GraphicsLabel}}	
		</b-dropdown-item>
	</div>
</template>

<script>
import lang from "../lang.js";

export default {
  name: 'MenuEntriesHouse',
  data(){
    var model = {
        lang: lang
    };
    
    return model;
  },
  computed: {
	getSelected() {
		console.log("getSelected", this.$store.state.selectedHouse);
		return this.$store.state.selectedHouse;
	}
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>