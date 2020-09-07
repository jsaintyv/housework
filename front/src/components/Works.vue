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
                <b-button class="dayhd" variant="light" v-on:click="openCreate(d)" >{{d | weekday}} {{d | formatDate }}</b-button>
                                
                <div class="work" v-for="w in workByDays[d.valueOf()]" v-bind:key="w.id" 
                            bg-variant="primary" text-variant="white">
                    <p>{{w.worker.login}}</p>
                    <p>{{w.type.name}}</p>
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
            var model = {
                lang: lang,               
                days: null,
                workByDays: null
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
            canRemove(w) {
                return this.$store.state.selectedHouse.owned||(w.worker.id === this.$store.state.currentUser.id)
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
    padding: 5px; 
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

.day {
    
    padding-right:5px;
    padding-left:5px;
    
}

@media (min-width:768px) {
    .day {
        flex: 0 0 14.28%;
        border-left-style: solid;
        border-left-width: 1px;
        border-left-color: #E0E0E0;
        height: 80vh;
    }
}

@media (max-width:767px) {
    .day {
        flex: 0 0 100%;
        
    }
}

</style>