<template>    
	<div v-if="getSelected != null">
		<p> {{ getSelected.name }}  ({{ getSelected.id}})</p>

		<b-card class="roundCard" :title="$lang.graphicsBoard.scoringToPaid">			
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

		<b-row>
			<b-col lg="6" sm="12">
				<b-card class="chartCard" :title="$lang.graphicsBoard.workByWeeks">			
					<b-row>
						

								<div class="summaryContainer">
									<canvas ref="weekSummary" ></canvas>	
								</div>

						
					</b-row>			
				</b-card>
			</b-col>
			<b-col lg="6"  sm="12">
				<b-card class="chartCard" :title="$lang.graphicsBoard.workByType">			
					<b-row>


								<div class="summaryContainer">
									<canvas ref="typeSummary" ></canvas>	
								</div>


					</b-row>						
				</b-card>
			</b-col>
		<b-row>
	</div>
	
</template>

<script>
import buildDiagramBar from "../utils/ChartJsCubeUtils"
import {ACTION_LOAD_SCORES_BY_USER, ACTION_LOAD_SCORE_CUBE} from "../stores/index.js";

export default {
  name: 'HouseInfo',
  computed: {
	  getSelected() {
		  return this.$store.state.selectedHouse;
	  },
	  getScoresByUser() {
		  return this.$store.state.scoresByUser;
	  },
	  getScoresCube() {
		  return this.$store.state.scoresCube;
	  }
  },
  watch: {
	  getSelected() {
		this.$store.dispatch(ACTION_LOAD_SCORES_BY_USER);
		this.$store.dispatch(ACTION_LOAD_SCORE_CUBE);		
	  },
	  getScoresCube() {		
		if(this.chartWeekSummary) {
			this.chart.destroy();
		}				

		this.chartWeekSummary = buildDiagramBar(
			this.$refs.weekSummary, 
			this.$store.state.scoresCube.filter((c)=> c.VALUE > 0), 
			(c)=>c.TEMPS,
			(c)=>c.USER
		);		

		this.chartTypeSummary = buildDiagramBar(
			this.$refs.typeSummary, 
			this.$store.state.scoresCube.filter((c)=> c.VALUE > 0), 
			(c)=>c.TASK_TYPE,
			(c)=>c.USER
		);		
	  }
  },
  mounted () {
	this.$nextTick(() => {
		console.log(this.$refs.weekSummary);		
		this.$store.dispatch(ACTION_LOAD_SCORES_BY_USER);
		this.$store.dispatch(ACTION_LOAD_SCORE_CUBE);		
  	});
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>


.roundCard .card-body {	
	margin:auto;	
	text-align: center;
}



.chartCard .card-body,.chartCard .row, .chartCard .summaryContainer  {	
	width:100%;
}

.chartCard .card-title {
	text-align: center;
}

.summaryContainer {
	display:block;
	position:relative;
	margin: auto;
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