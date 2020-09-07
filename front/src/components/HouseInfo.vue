<template>
    
	<div >
		<p v-if="getSelected != null"> {{ getSelected.name }}  ({{ getSelected.id}})</p>
		<div class="card">
			<div class="summaryContainer">
				<canvas ref="weekSummary" ></canvas>	
			</div>
		</div>
	</div>
	
</template>

<script>

import ChartJs from "chart.js";
import {default as store, ACTION_LOAD_SCORES_BY_USER} from "../stores/index.js";

export default {
  name: 'HouseInfo',
  computed: {
	  getSelected() {
		  return store.state.selectedHouse;
	  },
	  getScoresByUser() {
		  return store.state.scoresByUser;
	  }
  },
  watch: {
	  getSelected() {
		  store.dispatch(ACTION_LOAD_SCORES_BY_USER);
	  },
	  getScoresByUser() {		
		if(this.chart) {
			this.chart.destroy();
		}
		var labels = [];
		var values = [];
		store.state.scoresByUser.forEach((v)=> {
			labels.push(v.login);
			values.push(v.value);
		});

		var ctx = this.$refs.weekSummary.getContext('2d');
		this.chart = new ChartJs(ctx, {
			type: 'bar',
			data: {
				labels: labels,
				datasets: [{
					label: "Score",
					data: values,
					backgroundColor: 'rgba(255, 99, 132, 0.2)',						
					borderColor: 'rgba(255, 99, 132, 1)',						
					borderWidth: 1					
				}],
				
			},
			options: {
				 scales: {
                  	yAxes: [{
                            display: true,
                            ticks: {
                                beginAtZero: true,
                                
                                
                            }
                        }]
                },
			}
		});  	
		console.log(this.chart)	;
	  }
  },
  mounted () {
	this.$nextTick(() => {
		console.log(this.$refs.weekSummary);
		
		store.dispatch(ACTION_LOAD_SCORES_BY_USER);
		
  	});
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.summaryContainer {
	display:block;
	position:relative;
	width:60vw;	
}
</style>