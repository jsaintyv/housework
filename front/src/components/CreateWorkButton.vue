<template>
    <div v-if="getSelected != null">                
        <b-button @click="create()">create</b-button>
        <b-modal ref="mdlConfirmAdd" title="" @ok="add()">
            <div role="group">
                <label for="input-password">Who</label>
                <b-form-select v-model="worker" :options="getSelected.users" value-field="id" text-field="login" ></b-form-select>
            </div>
            <div role="group">
                <label for="input-password">Done</label>
                <b-form-select v-model="taskType" :options="getSelected.types" value-field="id" text-field="name"></b-form-select>
            </div>
            <div role="group">
                <label for="input-password">When</label>
                <b-form-input type="date" v-model="when"></b-form-input>
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
                    });
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>