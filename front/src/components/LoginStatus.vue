<template>
    
</template>

<script>
import jQuery from "jquery";
import lang from "../lang.js";

export default {
  name: 'Login',
  data(){
    return {
        lang: lang,
        login: "",
        password: "",
    };
  },
  methods: {
      callLogin () {
          jQuery.post('/api/user/login', {
              login: this.login,
              password: this.password
          })
          .done((r)=>{
             //console.log(r); 
          });
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
