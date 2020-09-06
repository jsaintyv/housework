<template>
    <em>{{getLogin}}</em>
</template>

<script>
import {ACTION_CHECK_CONNECTED} from '../stores/index'

export default {  
  name: "LoginStatus",
  data() {    
    console.log(this.$lang);
    this.$store.dispatch(ACTION_CHECK_CONNECTED);
  },
 computed: {
    getLogin() {
      return this.$store.state.currentUser ? this.$store.state.currentUser.login : "";
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
