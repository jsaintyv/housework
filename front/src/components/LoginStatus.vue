<template>
    <em>{{login}}</em>
</template>

<script>
import UserService from "../services/UserService.js";
import lang from "../lang.js";

export default {  
  name: "LoginStatus",
  data() {
    var r = {
        lang: lang,
        login: ""
    };   
    UserService
      .currentUser()
      .done((u)=>r.login=u.login);  
    return r; 
  },
  methods: {      
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
