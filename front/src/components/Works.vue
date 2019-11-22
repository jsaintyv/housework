<template>
    <div v-if="getSelected != null">
        <div>Works</div>
        <b-input type="date" :value="getEndTime" @change="changeDate" />
        <div v-if="getWorks == null">
            Loading...
        </div>
        <div v-if="getWorks != null">
            <b-row v-for="w in getWorks" v-bind:key="w.id">
                <b-col>
                    {{w.id}}
                </b-col>
                <b-col>
                    <b-button @click="remove(w)">-</b-button>
                </b-col>
            </b-row>
        </div>
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
        REMOVE_WORK,
        ADD_WORK,
        CHANGE_SELECTED_PERIOD,
        WORKS_UPDATE
    } from "../stores/index.js";
    import lang from "../lang.js";
    import WorkService from "../services/WorkService.js";
    import DateUtils from "../utils/DateUtils.js";

    export default {
        name: 'Works',
        data() {
            var model = {
                lang: lang,
                worker: null,
                taskType: null,
                when: null
            };
            return model;
        },
        computed: {
            getSelected() {
                return store.state.selectedHouse;
            },
            getStartTime() {
                return store.state.startTime;
            },
            getEndTime() {
                return store.state.endTime;
            },
            getLoadingWorks() {
                return store.state.loadingWorks;
            },
            getWorks() {
                return store.state.works;
            },
        },
        methods: {
            changeDate(e) {
                var selected = DateUtils.convertInputValue(e);
                var endTime = DateUtils.addDay(selected, +1).getTime();
                var startTime = DateUtils.addDay(selected, -7).getTime();
                console.log(this); 
                store.commit(CHANGE_SELECTED_PERIOD, selected);
                WorkService.list(store.state.selectedHouse.id, startTime, endTime)
                .done(list => {
                    store.commit(WORKS_UPDATE, list);
                });
            },
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
            },
            remove(w) {
                WorkService.remove(store.state.selectedHouse.id, w.id)
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