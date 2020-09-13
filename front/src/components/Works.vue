<template>
    <div v-if="getSelected != null">
        <b-row> 
            <b-col><b-input type="date" :value="getCurrentTime" @change="changeDate" /></b-col>
            <b-col><CreateWorkButton ref="createButton" /></b-col>
        </b-row>
        <div class="loading" v-if="getWorks == null">
            Loading...
        </div>
        <b-row v-if="days">
            <b-col class="day" v-for="d in days" v-bind:key="d.valueOf()">
                <b-button class="dayhd" variant="light" v-on:click="openCreate(d)" v-if="canAdd(d)" >{{d | weekday}} {{d | formatDate }}</b-button>
                <div class="dayhd" variant="light" v-if="!canAdd(d)" >{{d | weekday}} {{d | formatDate }}</div>
                                
                <div class="work" v-for="w in workByDays[d.valueOf()]" v-bind:key="w.id" 
                            bg-variant="primary" text-variant="white">
                    <span class="circle" v-bind:style="{ backgroundColor: w.type.colorRgb}">{{w.type.shortName}}</span>
                    <p class="textRow">{{w.worker | displayName}}</p>
                    <p class="textRow">{{w.type.name}}</p>
                    <p class="topright" v-if="canRemove(w)"  @click="remove(w)"><span class="material-icons">clear</span></p>
                </div>   
            </b-col>
        </b-row>       
    </div>
</template>

<script>
    import {
        default as store,
        REMOVE_WORK,        
        ACTION_CHANGE_DATE
    } from "../stores/index.js";
    import lang from "../lang.js";
    import WorkService from "../services/WorkService.js";
    import DateUtils from "../utils/DateUtils.js";
    import CreateWorkButton from "./CreateWorkButton";

    export default {
        name: 'Works',
        data() {
            var limitDate = new Date();
            limitDate.setDate(limitDate.getDate()-2);

            var maxDate = new Date();
            maxDate.setDate(maxDate.getDate()+ 1);

            var model = {
                lang: lang,               
                days: null,
                workByDays: null,
                limitDate: limitDate.getTime(),
                maxDate: maxDate.getTime()
            };
            return model;
        },
        components: {
            "CreateWorkButton": CreateWorkButton
        },
        computed: {
            getSelected() {
                return store.state.selectedHouse;
            },
            getCurrentTime() {
                var d = store.state.currentTime;
                if(d == null)
                    return null;
                return DateUtils.formatDateISO(d);
            },
            getLoadingWorks() {
                return store.state.loadingWorks;
            },
            getWorks() {
                return store.state.works;
            },
            
        },        
        watch: {
            getWorks() {                
                if(store.state.currentTime == null) {
                    this.$data.days = null;
                    this.$data.workByDays = {};
                    return;
                }

                this.$data.days = DateUtils.build7DayBefore(store.state.currentTime);
                
                this.$data.workByDays={};
                this.$data.days.forEach(d => {
                    this.$data.workByDays[d.valueOf()] = DateUtils.filterByDay(d, store.state.works, (w)=>{
                        return new Date(w.date);
                    });
                });
            }
        },
        methods: {
            changeDate(e) {                
                var selected = DateUtils.convertInputValue(e);
                this.$store.dispatch(ACTION_CHANGE_DATE, selected);                
            },           
            remove(w) {
                WorkService.remove(store.state.selectedHouse.id, w)
                    .done(() => {
                        store.commit(REMOVE_WORK, w);
                    });
            },
            canAdd(d) {       
                console.log(this.limitDate, d.getTime(), this.maxDate);         
                return this.$store.state.selectedHouse.owned
                    || ((this.limitDate <= d.getTime()) && (d.getTime() <= this.maxDate))
                    
            },
            canRemove(w) {                
                return this.$store.state.selectedHouse.owned
                    || ((w.date >= this.limitDate) && (w.date <= this.maxDate) && w.worker.id === this.$store.state.currentUser.id && !w.type.reservedToAdmin);                                        
            },
            openCreate(d) {
                

                this.$refs.createButton.showDialog(d);
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

.work {
    position:relative; /* Allow absolute for cross */
    border:1px solid #e0e0e0;
    border-radius: 5px;
    margin-bottom: 5px;   
    padding-top: 10px; 
    padding-left: 45px;
    padding-right: 20px;
    
}

.topright {
    position: absolute;
    right:2px;
    top:-2px;
}

.dayhd {
    text-align: center;
    line-height: 20px;
    font-size: 16px;
    width: 100%;
    margin-top: 10px;
    margin-bottom: 10px;
}

div.dayhd {
    line-height: 32px;
    vertical-align: middle;
}


.day {
    
    padding-right:5px;
    padding-left:5px;
    
}

.textRow {
    text-overflow: ellipsis;
    line-height: 15px;
    overflow: hidden;
}

.circle {
    display: block;
    position:absolute;
    border: 0px;
    line-height: 30px;
    width: 30px;
    height: 30px;    
    border-radius: 30px;
    overflow: hidden;
    text-align: center;
    left:5px;
    top:18px;
    color: #E0E0E0;
    font-size: 12px;
}

@media (min-width:1250px) {
    .day {
        flex: 0 0 14%;
        border-left-style: solid;
        border-left-width: 1px;
        border-left-color: #E0E0E0;
        height: 80vh;
        max-width: 14%;
    }
}

@media (min-width: 768px) and (max-width:1250px) {
    .day {
        flex: 0 0 50%;
        border-left-style: solid;
        border-left-width: 1px;
        border-left-color: #E0E0E0;        
        max-width: 50%;
    }
}

@media (max-width:767px) {
    .day {
        flex: 0 0 100%;
        
    }
}

</style>