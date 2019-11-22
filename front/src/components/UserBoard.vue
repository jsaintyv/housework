<template>
  <div>
    <UserHouses />
    <HouseInfo />
    <TaskTypes />
    <WorkersList />
    <Works />
  </div>
</template>

<script>
  import UserHouses from "./UserHouses.vue";
  import HouseInfo from "./HouseInfo.vue";
  import TaskTypes from "./TaskTypes.vue";
  import WorkersList from "./WorkersList.vue";
  import Works from "./Works.vue";

  export default {
    name: 'UserBoard',
    components: {
      'UserHouses': UserHouses,
      'HouseInfo': HouseInfo,
      'TaskTypes': TaskTypes,
      'WorkersList': WorkersList,
      'Works': Works
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>