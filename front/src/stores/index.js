import Vue from 'vue';
import Vuex from 'vuex';
import DateUtils from '../utils/DateUtils';
import WorkService from "../services/WorkService.js";
import HouseService from "../services/HouseService.js";
import UserService from "../services/UserService.js";

Vue.use(Vuex);

export const DISCONNECTED = "DISCONNECTED";
export const CONNECTED = "CONNECTED";
export const UPDATE_HOUSES = "UPDATE_HOUSES";
export const REMOVE_HOUSE = "REMOVE_HOUSE";
export const SELECT_HOUSE = "SELECT_HOUSE";
export const ADD_TASK_TYPE = "ADD_TASK_TYPE";
export const REMOVE_TASK_TYPE = "REMOVE_TASK_TYPE";
export const CHANGE_SELECTED_PERIOD = "CHANGE_SELECTED_PERIOD";
export const WORKS_UPDATE = "WORKS_UPDATE";
export const ADD_WORK = "ADD_WORK";
export const REMOVE_WORK = "REMOVE_WORK";

export const ACTION_CHANGE_DATE = "CHANGE_DATE";
export const ACTION_SELECT_HOUSE_ID = "SELECT_HOUSE_ID";
export const ACTION_CHECK_CONNECTED = "CHECK_CONNECTED";

export const STATUS_CHECKING = 0;
export const STATUS_DISCONNECTED = 1;
export const STATUS_CONNECTED = 2;


export default new Vuex.Store({
    state: {
        stateConnected: STATUS_CHECKING,
        currentUser: null,
        houses: [],
        selectedHouse: null,
        currentTime: new Date(),
        works: null
    },    
    mutations: { 
        [CONNECTED] (state, user) {
            console.log("Connected", user);
            state.stateConnected = STATUS_CONNECTED;
            state.currentUser = user;
        },
        [DISCONNECTED] (state) {
            console.log("Disconnected");
            state.stateConnected = STATUS_DISCONNECTED;
            state.currentUser = null;
        },
        [UPDATE_HOUSES] (state, houses) {
            state.houses = houses;
            if(houses.indexOf(state.selectedHouse) < 0) {
                state.selectedHouse = null;
            }
        },
        [SELECT_HOUSE] (state, house) {
            state.selectedHouse = house;
            state.works = null;
            if(state.currentTime == null) {
                state.currentTime = DateUtils.removeTime(new Date());
            }
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
        [CHANGE_SELECTED_PERIOD] (state, currentTime) {
            state.currentTime = currentTime;
            state.works = null;
        },
        [WORKS_UPDATE] (state, works) {
            state.works = works;
        },
        [ADD_WORK] (state, work) {
            if(state.works != null)
                state.works.push(work);
        },
        [REMOVE_WORK] (state, work) {
            if(state.works != null)
                state.works = state.works.filter((w)=>w.id != work.id);
        }
    },
    actions: {
        /**
         * Change date
         * @param {Vuex.Store} store
         * @param {Date} selected
         */
        [ACTION_CHANGE_DATE] (store, selected) {
            if(selected == null) {
                selected = store.state.currentTime;
                if(selected == null) {
                    selected = DateUtils.removeTime(new Date());
                }
            }
            
            var endTime = DateUtils.addDay(selected, +1).getTime();
            var startTime = DateUtils.addDay(selected, -7).getTime();            
            store.commit(CHANGE_SELECTED_PERIOD, selected);
            WorkService.list(store.state.selectedHouse.id, startTime, endTime)
            .done(list => {
                store.commit(WORKS_UPDATE, list);
            });
        },
        [ACTION_CHECK_CONNECTED] (store) {
            UserService
            .currentUser()
            .done((u)=> {
                if(u.id) {
                    store.commit(CONNECTED, u);
                } else {
                    store.commit(DISCONNECTED);
                }
            })
        },
        /**
         * Change selected house Id
         * @param {Vuex.Store} store
         * @param {Int} houseId
         */
        [ACTION_SELECT_HOUSE_ID] (store, houseId) {
            if(store.state.selectedHouse && store.state.selectedHouse.id == houseId) {
                return; // do nothing
            }

            var commitHouseCallback = ()=> {
                console.log("Seek in ", store.state.houses, houseId);
                store.state.houses.filter((h)=> {
                    if(h.id == houseId) {
                        console.log("HouseId", houseId);
                        store.commit(SELECT_HOUSE, h);
                    }
                });
            };

            
            console.log(store.state.houses);
            if(! store.state.houses || store.state.houses.length == 0) {
                console.log("Load houses....");
                HouseService
                .listOwned()
                .done((l)=>{
                    console.log("Loaded", l);
                    store.commit(UPDATE_HOUSES, l);
                    commitHouseCallback();
                });
            } else {
                commitHouseCallback();
            }
        }
    }

});
