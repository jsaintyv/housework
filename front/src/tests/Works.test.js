import { mount, createLocalVue } from '@vue/test-utils';
import BootstrapVue from 'bootstrap-vue'
import { default as store,
         CONNECTED,
         SELECT_HOUSE,
         CHANGE_SELECTED_PERIOD,
         WORKS_UPDATE } from '../stores/index.js';
import Works from '../components/Works.vue';
import { UserAdmin, SampleHouse, DATE_2412, WORKS_2412} from './TestFactory.js';
import DateUtils from '../utils/DateUtils';



// create an extended `Vue` constructor
const localVue = createLocalVue()

import initVue from '../initVue'

// install plugins as normal
localVue.use(BootstrapVue);
initVue(localVue);

describe('Works', () => {
    // Now mount the component and you have the wrapper
    const wrapper = mount(Works, {localVue});

    it('no crash', async () => {
         expect(wrapper.contains('button')).toBe(false);
         store.commit(CONNECTED, UserAdmin);
         store.commit(SELECT_HOUSE, SampleHouse);         
         await localVue.nextTick();         
         expect(wrapper.find(".loading")).not.toBeNull();
         console.log("Change selected");
         store.commit(CHANGE_SELECTED_PERIOD, DATE_2412);
         console.log("Change works");
         store.commit(WORKS_UPDATE, WORKS_2412);
         await localVue.nextTick();
         expect(wrapper.text()).toContain('hugo');
         expect(wrapper.text()).toContain('marie');
    });
});