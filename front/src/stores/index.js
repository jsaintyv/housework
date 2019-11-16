import Vue from 'vue';
import Vuex from 'vuex';


Vue.use(Vuex);


export const CONNECTED = "CONNECTED";
export const UPDATE_HOUSES = "UPDATE_HOUSES";
export const REMOVE_HOUSE = "REMOVE_HOUSE";
export const SELECT_HOUSE = "SELECT_HOUSE";
export const ADD_TASK_TYPE = "ADD_TASK_TYPE";
export const REMOVE_TASK_TYPE = "REMOVE_TASK_TYPE";

export default new Vuex.Store({
    state: {
        currentUser: null,
        houses: [],
        selectedHouse: null
    },
    mutations: { 
        [CONNECTED] (state, user) {
            state.currentUser = user;
        },
        [UPDATE_HOUSES] (state, houses) {
            state.houses = houses;
            if(houses.indexOf(state.selectedHouse) < 0) {
                state.selectedHouse = null;
            }
        },
        [SELECT_HOUSE] (state, house) {
            state.selectedHouse = house;
        },
        [REMOVE_HOUSE] (state, removed) {
            state.houses = state.houses.filter((h)=>h.id != removed.id);
        },
        [ADD_TASK_TYPE] (state, taskType) {
            state.selectedHouse.types.push(taskType);
        },
        [REMOVE_TASK_TYPE] (state, taskType) {
            state.selectedHouse.types = state.selectedHouse.types.filter((t)=>t.id != taskType.id);
        },
    }
});
