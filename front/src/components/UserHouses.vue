<template>
	<div>
	    <ul>
			<li v-for="h in owned" v-bind:key="h.id">
				{{ h.name }}  ({{h.id}}) <b-button @click="remove(h)">-</b-button>
				
			</li>
		</ul>
		<b-button @click="create()">{{lang.CreateHouseLabel}}</b-button>
		<b-modal ref="mdlConfirmRemove" title="BootstrapVue" @ok="removeConfirmed()">
			<p>{{lang.PleaseConfirmRemoveOf}}</p>
		</b-modal>
		<b-modal ref="mdlConfirmAdd" title="BootstrapVue" @ok="createConfirmed()">
			<div role="group">
             <label for="input-password">{{lang.NameLabel}}:</label>
             <b-form-input
	              type="string"
	              v-model="newName"	      	     	      
	              trim
	        	  ></b-form-input>
    	 	</div>
		</b-modal>
		
	</div>
</template>

<script>
import HouseService from "../services/HouseService.js";
import lang from "../lang.js";

export default {
  name: 'UserHouses',
  data(){
    var model = {
        lang: lang,
        owned: [],
        availables: [],
        selectedToRemove: null,
        newName: ""
    };
    
    
    return model;
  },
  created() {
      this.loading();
  },
  methods: {
      loading() {
          var self = this;
          HouseService.listOwned()
          	.done((l)=>{
          	    l.forEach((h)=>{
          	        self.owned.push(h)
          	    });
          	});
      },
      create() {
          this.$refs['mdlConfirmAdd'].show();
      },
      createConfirmed() {
       	HouseService
       		.create({name: this.newName})
       		.done((h)=> this.owned.push(h));
      },
      remove(h) {
          this.selectedToRemove = h;
          this.$refs['mdlConfirmRemove'].show();
      },
      removeConfirmed() {
          HouseService
          	.remove(this.selectedToRemove.id)
          	.done(()=> {
          		this.owned.splice(this.owned.indexOf(this.selectedToRemove), 1);	    
          	});
      }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
