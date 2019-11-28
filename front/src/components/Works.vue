<template>
    <div v-if="getSelected != null">        
        <b-input type="date" :value="getCurrentTime" @change="changeDate" />
        <div class="loading" v-if="getWorks == null">
            Loading...
        </div>
        <b-row v-if="days">
            <b-col v-for="d in days" v-bind:key="d.valueOf()">
                <p>{{d | formatDate }}</p>
                <b-card-group deck>
                     <b-card v-for="w in workByDays[d.valueOf()]" v-bind:key="w.id" 
                             bg-variant="primary" text-variant="white"  class="text-center">
                        <b-card-text>{{w.worker.login}}/{{w.type.name}}</b-card-text>
                        <b-button @click="remove(w)">-</b-button>
                    </b-card>
                </b-card-group>   
            </b-col>
        </b-row>
        <CreateWorkButton />       
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
                    this.$data.workByDays[d.valueOf()] = DateUtils.filterByDay(d, store.state.works, (w)=>new Date(w.date));   
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
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>