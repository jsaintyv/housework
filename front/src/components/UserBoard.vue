<template>
  <div id="board">
    <div v-if="getSelected == null">
      Veuillez sélectionner une maison.
    </div>
    <div v-if="getSelected != null">
      <b-dropdown-item v-for="h in getHouses" v-bind:key="h.id" @click="select(h)">
        <router-link :to="{name: 'houseBoard', params: {houseId: h.id}}">{{h.name}} </router-link> 	
      </b-dropdown-item>
    </div>
  </div>
</template>

<script>

  export default {
    name: 'UserBoard',
    computed: {
        getSelected() {
            return this.$store.state.selectedHouse;
        },
        getHouses() {
          return this.$store.state.houses;
        }
    },
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

#board {
  margin-left:5px;
  margin-right:5px;
}


</style>