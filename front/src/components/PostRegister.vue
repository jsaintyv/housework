<template>
    <b-card id="hw-login" class="text-left">
    	<b-card-text>
        	<div>{{lang.PostRegisterLabel}}</div>
        </b-card-text>
  </b-card>
</template>

<script>
import lang from "../lang.js";

export default {
  name: 'PostRegister',
  data(){
    return {
        lang: lang
    };
  },
  methods: {
      	
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
</style>
