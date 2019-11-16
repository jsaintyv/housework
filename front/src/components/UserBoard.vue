<template>
  <div>
    <UserHouses />
    <HouseInfo />
    <TaskTypes />
  </div>
</template>

<script>
  import UserHouses from "./UserHouses.vue";
  import HouseInfo from "./HouseInfo.vue";
  import TaskTypes from "./TaskTypes.vue";

  export default {
    name: 'UserBoard',
    components: {
      'UserHouses': UserHouses,
      'HouseInfo': HouseInfo,
      'TaskTypes': TaskTypes
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>