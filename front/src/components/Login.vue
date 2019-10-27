<template>
    <b-card id="hw-login" class="text-left">
    	<b-card-text>
        <div role="group">
            <label for="input-login">{{lang.LoginLabel}}:</label>
            <b-form-input
              id="input-login"
              v-model="login"	      	     	     
              trim>
            </b-form-input>
         </div>
         <div role="group">
             <label for="input-password">{{lang.PasswordLabel}}:</label>
             <b-form-input
              type="password"
              id="input-password"
              v-model="password"	      	     	      
              trim
        	  ></b-form-input>
    	 </div>  
    	 </b-card-text>
    	     	  
    	 <div id="hw-button" role="group">
    	     <b-button @click="callLogin()">{{lang.ConnectionLabel}}</b-button>    	 
    	     <b-button @click="callRegister()">{{lang.RegisterLabel}}</b-button>
    	 </div>
    	 
  </b-card>
</template>

<script>
import UserService from "../services/UserService.js";
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
         console.log(UserService);
         UserService
         	.login(this.login, this.password)
         	.done((r)=> {
				console.log(r);
				
				if(r != false) {
					this.$router.push('/board');
				} else {
				    alert("Error");
				}
         	});                   
      },
      callRegister() {
          this.$router.push('/register');
      }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

#hw-login {
	position:absolute;
	top:20%;
	left:20%;
	right:20%;
	
}

button {
	margin-right: 5px;
}

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
