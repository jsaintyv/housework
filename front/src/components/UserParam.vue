<template>
  <div>        
    <b-form-group :label="$lang.userParam.name">
            <b-form-input
              id="input-"              
              v-model="getCurrentUser.name"
              type="text"
              required              
            ></b-form-input>
    </b-form-group>    

    <b-form-group :label="$lang.userParam.newPassword">
            <b-form-input
              id="input-"              
              v-model="newPassword"
              type="text"
              required              
            ></b-form-input>
    </b-form-group>    
    <b-form-group :label="$lang.userParam.confirmPassword">
            <b-form-input
              id="input-"              
              v-model="confirmPassword"
              type="text"
              required              
            ></b-form-input>
    </b-form-group>    
    <b-button @click="update()">{{$lang.userParam.save}}</b-button>
  </div>
</template>

<script>
import UserService from "../services/UserService.js";

export default {
  name: "UserParam",
  data: ()=>{ 
    return {
      newPassword: "",
      confirmPassword: ""
    };
  },
  computed: {
    getCurrentUser() {
      return this.$store.state.currentUser;
    }
  },
  methods: {    
    update() {
      UserService.update(this.$store.state.currentUser, (!!this.newPassword && this.newPassword===this.confirmPassword) ? this.newPassword : null);
      this.newPassword = "";
      this.confirmPassword = "";
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>