
import Vue from 'vue';
import { mount, createLocalVue } from '@vue/test-utils';
import BootstrapVue from 'bootstrap-vue'
import {
    default as store,
    SELECT_HOUSE, CONNECTED
} from '../stores/index.js';
import TaskTypes from '../components/TaskTypes.vue';
import { UserAdmin, SampleHouse } from './TestFactory.js';
import initVue from '../initVue'

// create an extended `Vue` constructor
const localVue = createLocalVue();


// install plugins as normal
localVue.use(BootstrapVue)

initVue(localVue);

describe('TaskTypes', () => {
    // Now mount the component and you have the wrapper


    it('no house no button', () => {
        const wrapper = mount(TaskTypes, { localVue });
        var taskType = wrapper.findComponent(TaskTypes);
        expect(wrapper.find('button').exists()).toBe(false);
    });

    test('when house is selected', async () => {
        const wrapper = mount(TaskTypes, { localVue });

        var taskType = wrapper.findComponent(TaskTypes);
        expect(taskType.exists()).toBe(true)

        store.commit(CONNECTED, UserAdmin);
        store.commit(SELECT_HOUSE, SampleHouse);

        expect(store.state.selectedHouse.owned).toBe(true);

        await localVue.nextTick()

        expect(wrapper.find('button').exists()).toBe(true);
    });
});