<template>
  <div class="hw-login">
    <div role="group">
	    <label for="input-login">Login:</label>
	    <b-form-input
	      id="input-login"
	      v-model="login"	      	     	     
	      trim
	    ></b-form-input>
    </div>
     <div role="group">
	    <label for="input-login">Mot de passe:</label>
	    <b-form-input
	      id="input-password"
	      v-model="password"	      	     	      
	      trim
	    ></b-form-input>
    </div>
    
    <b-button>Connexion</b-button>    
  </div>
</template>

<script>
export default {
  name: 'Login',
  props: {
    login: String,
    password: String,
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
