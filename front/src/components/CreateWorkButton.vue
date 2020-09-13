<template>
    <div v-if="getSelected != null">                
        <b-button @click="create()">{{$lang.CreateWork}}</b-button>
        <b-modal ref="mdlConfirmAdd" title="" @ok="add()">
            <div role="group">
                <label for="login">{{$lang.CreateWho}}</label>
                <b-form-select id="login" v-model="worker" :options="getSelected.users" value-field="id" text-field="login" ></b-form-select>
            </div>
            <div role="group">
                <label for="task">{{$lang.CreateTask}}</label>
                <b-form-select id="task" v-model="taskType" :options="getAvailablesTypes" value-field="id" text-field="name"></b-form-select>
            </div>
            <div role="group">
                <label for="when">{{$lang.CreateWhen}}</label>
                <b-form-input id="when" type="date" v-model="when"></b-form-input>
            </div>
        </b-modal>
    </div>
</template>

<script>
    import {
        default as store,        
        ADD_WORK,                
    } from "../stores/index.js";
    import lang from "../lang.js";
    import WorkService from "../services/WorkService.js";
    import DateUtils from "../utils/DateUtils.js";

    export default {
        name: 'Works',
        data() {
            var model = {
                lang: lang,
                worker: this.$store.state.currentUser.id,
                taskType: null,
                when: null,

            };
            return model;
        },
        computed: {
            getSelected() {
                return store.state.selectedHouse;
            },            
            getAvailablesTypes() {                                                
                var selected  = store.state.selectedHouse;
                var owned = selected.owned; 
                return selected.types.filter((t)=>owned || !t.reservedToAdmin);
            }
        },        
        methods: {            
            create() {
                this.$refs['mdlConfirmAdd'].show();
            },
            add() {
                WorkService.create(store.state.selectedHouse.id, {
                        workerId: this.worker,
                        taskTypeId: this.taskType,
                        date: DateUtils.convertInputValue(this.when).getTime()
                    })
                    .done((w) => {
                        store.commit(ADD_WORK, w);
                    })
                    .fail(()=> {
                        this.$bvToast.toast(
                            this.$lang.forbidden, 
                            {
                            title: this.$lang.forbidden,
                            variant: 'danger',
                            autoHideDelay: 5000,
                            solid: true
                        });
                    });
            },
            showDialog(d) {                
                this.when = DateUtils.formatDateISO(d);
                console.log(d, this.when);
                this.$refs['mdlConfirmAdd'].show();
                
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>