<template>
  <b-form @submit="callLogin();return false;" @submit.stop.prevent >
  <b-card id="hw-login" class="text-left">
    <b-card-text>
      <div role="group">
        <label for="input-login">{{lang.LoginLabel}}:</label>
        <b-form-input id="input-login" v-model="login" trim></b-form-input>
      </div>
      <div role="group">
        <label for="input-password">{{lang.PasswordLabel}}:</label>
        <b-form-input type="password" id="input-password" v-model="password" trim></b-form-input>
      </div>
    </b-card-text>

    <b-row >
      <b-col md="2"><b-button type="submit" >{{lang.ConnectionLabel}}</b-button></b-col>
      <b-col md="2"><b-button @click="callRegister()">{{lang.RegisterLabel}}</b-button></b-col>
    </b-row>
  </b-card>
  </b-form>
</template>

<script>
import UserService from "../services/UserService.js";
import { CONNECTED, ACTION_LOAD_HOUSES } from "../stores/index";
import lang from "../lang.js";

export default {
  name: "Login",
  data() {
    return {
      lang: lang,
      login: "",
      password: ""
    };
  },
  methods: {
    callLogin(e) {
      console.log(UserService);
      UserService.login(this.login, this.password).done(r => {
        this.$store.commit(CONNECTED, r);
        console.log(r);
        this.$store.dispatch(ACTION_LOAD_HOUSES, () => {
          if (this.$store.state.houses.length == 1) {
            this.$store.dispatch(ACTION_LOAD_HOUSES, () => {              
              this.$router.push("/houseBoard/" + this.$store.state.houses[0].id);
            });
          }
        });
        if (r != false) {
          this.$router.push("/board");
        } else {
          alert("Error");
        }
      });
      e.stopPropagation();
      e.preventDefault();
    },
    callRegister() {
      this.$router.push("/register");
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#hw-login {
  position: absolute;
  top: 20%;
  left: 20%;
  right: 20%;
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
