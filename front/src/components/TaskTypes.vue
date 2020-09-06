<template>
    <div v-if="getSelected != null">
        <div>Tasks configuration</div>
        <b-row v-for="t in getSelected.types" v-bind:key="t.id">
            <b-col>
                ({{t.id}})
            </b-col>    
            <b-col>
                {{t.name}}
            </b-col>
            <b-col>
                <b-form-input id="input-" :readonly="!getSelected.owned" v-model="t.value" type="number" required @change="update(t)"></b-form-input>                
            </b-col>
            <b-col>
                <b-form-input id="input-" :readonly="!getSelected.owned" v-model="t.name" type="text" required @change="update(t)"></b-form-input>
            </b-col>
            <b-col><b-button v-if="getSelected.owned" @click="remove(t)">-</b-button></b-col>
        </b-row>
        <b-button v-if="getSelected.owned" @click="create()">{{$lang.CreateTaskType}}</b-button>
    </div>
</template>

<script>
    import {
        default as store,
        ADD_TASK_TYPE,
        REMOVE_TASK_TYPE
    } from "../stores/index.js";
    
    import TaskTypeService from "../services/TaskTypeService.js";

    export default {
        name: 'TaskTypes',
        computed: {
            getSelected() {
                return store.state.selectedHouse;
            }
        },
        methods: {
            create() {
                TaskTypeService.create(store.state.selectedHouse.id, {name:'new', value:'0'})
                .done((taskType)=> {
                    store.commit(ADD_TASK_TYPE, taskType);
                });
            },
            update(taskType) {
                TaskTypeService.update(store.state.selectedHouse.id, taskType)
            },
            remove(taskType) {
                TaskTypeService.remove(store.state.selectedHouse.id, taskType)
                .done((taskType)=> {
                    store.commit(REMOVE_TASK_TYPE, taskType);
                });
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>