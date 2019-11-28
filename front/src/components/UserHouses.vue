<template>
	<div>
		<b-dropdown-item v-for="h in getHouses" v-bind:key="h.id" @click="select(h)">
			{{ h.name }}  ({{h.id}}) <b-button @click="remove(h)">-</b-button>	
		</b-dropdown-item>
		<b-dropdown-item  @click="create()">{{lang.CreateHouseLabel}}</b-dropdown-item>
		<b-modal ref="mdlConfirmRemove" title="" @ok="removeConfirmed()">
			{{lang.PleaseConfirmRemoveOf}}
		</b-modal>
		<b-modal ref="mdlConfirmAdd" title="" @ok="createConfirmed()">
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
import {default as store, SELECT_HOUSE, UPDATE_HOUSES, REMOVE_HOUSE, ACTION_CHANGE_DATE} from "../stores/index.js";

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
  computed: {
	  getHouses() {
		  return store.state.houses;
	  }
  },
  methods: {
      loading() {
          HouseService.listOwned()
          	.done((l)=>{
				store.commit(UPDATE_HOUSES, l);
          	});
      },
      create() {
          this.$refs['mdlConfirmAdd'].show();
      },
      createConfirmed() {
       	HouseService
       		.create({name: this.newName})
       		.done((h)=> {
			    store.commit(UPDATE_HOUSES, [...store.state.houses, h]);
       		    this.$bvToast.toast('Add success', {
       		        variant: 'success',
       		        solid: true
       		    });
       		});
      },
      remove(h) {
          this.selectedToRemove = h;
          this.$refs['mdlConfirmRemove'].show();
      },
      removeConfirmed() {
          HouseService
          	.remove(this.selectedToRemove.id)
          	.done(()=> {
          		store.commit(REMOVE_HOUSE, this.selectedToRemove);	    
          	});
	  },
	  select(h) {
		  store.commit(SELECT_HOUSE, h);
		  store.dispatch(ACTION_CHANGE_DATE, store.state.currentTime);
	  }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>