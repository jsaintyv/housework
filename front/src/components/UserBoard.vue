<template>
  <div id="board">
    <div v-if="getSelected == null">
      Veuillez sélectionner une maison.
    </div>
    <div  v-if="getSelected != null" class="info">
      <b-tabs id="infotab" content-class="mt-3">
        <b-tab title="Info">
          <b-card-text><HouseInfo /></b-card-text>
        </b-tab>
        <b-tab  title="Types">
          <b-card-text><TaskTypes /></b-card-text>
        </b-tab>
        <b-tab title="Workers">
          <b-card-text><WorkersList /></b-card-text>
        </b-tab>
        <b-tab title="Works">
          <b-card-text><Works /></b-card-text>
        </b-tab>
      </b-tabs>
      </div>
  </div>
</template>

<script>
  import HouseInfo from "./HouseInfo.vue";
  import TaskTypes from "./TaskTypes.vue";
  import WorkersList from "./WorkersList.vue";
  import Works from "./Works.vue";

  export default {
    name: 'UserBoard',
    computed: {
        getSelected() {
            return this.$store.state.selectedHouse;
        }
    },
    components: {
      'HouseInfo': HouseInfo,
      'TaskTypes': TaskTypes,
      'WorkersList': WorkersList,
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