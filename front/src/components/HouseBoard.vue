<template>
  <div id="board">
    <div v-if="getSelected == null">
      Veuillez sélectionner une maison.
    </div>
    <div  v-if="getSelected != null" class="info">
      <Works />
    </div>
  </div>
</template>

<script>
  import Works from "./Works.vue";
  import {
    default as store
  } from "../stores/index.js";

  export default {
    name: 'HouseBoard',
    computed: {
        getSelected() {
            return store.state.selectedHouse;
        }
    },
    components: {
      'Works': Works
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#board {
  margin-left:5px;
  margin-right:5px;
}
</style>