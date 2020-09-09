<template>
    
	<div v-if="getSelected != null">
		<p> {{ getSelected.name }}  ({{ getSelected.id}})</p>

		<b-card>
			<b-row>		
				<div class="roundContainer" v-for="r in getScoresByUser" v-bind:key="r.login" col="1">
					<div class="round">
						{{r.value}}
					</div>
					<div  >
						{{r | displayName}}
					</div>
				</div>		
			</b-row>
		</b-card>

		<b-card>
			<b-row>
				<b-col col="12">
					<b-card>
						<div class="summaryContainer">
							<canvas ref="weekSummary" ></canvas>	
						</div>
					</b-card>
				</b-col>
			</b-row>
		</b-card>
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

.round {
	margin:auto;
	display: block;
	width: 100px;
	height: 100px;
	line-height: 100px;
	text-align: center;
	background-color: #5bc0de;
	color: #e0e0e0;
	border-radius: 100px;
}

.roundContainer {
	flex:none;
	width: 200px;
	text-align: center;
	margin-bottom: 10px;
}
</style>