import { mount, createLocalVue } from '@vue/test-utils';
import BootstrapVue from 'bootstrap-vue'
import { default as store,
         SELECT_HOUSE,
         CHANGE_SELECTED_PERIOD,
         WORKS_UPDATE } from '../stores/index.js';
import Works from '../components/Works.vue';
import { SampleHouse, DATE_2412, WORKS_2412} from './TestFactory.js';
import DateUtils from '../utils/DateUtils';



// create an extended `Vue` constructor
const localVue = createLocalVue()


localVue.filter('formatDate', function(value) {
    if (value) {
      return value.getDate() + "/" + value.getMonth() + "/" + value.getYear();
    }
  });

// install plugins as normal
localVue.use(BootstrapVue)

describe('Works', () => {
    // Now mount the component and you have the wrapper
    const wrapper = mount(Works, {localVue});

    it('no crash', () => {
         expect(wrapper.contains('button')).toBe(false);
         store.commit(SELECT_HOUSE, SampleHouse);
         expect(wrapper.find(".loading")).not.toBeNull();
         console.log("Change selected");
         store.commit(CHANGE_SELECTED_PERIOD, DATE_2412);
         console.log("Change works");
         store.commit(WORKS_UPDATE, WORKS_2412);

         expect(wrapper.text()).toContain('hugo');
         expect(wrapper.text()).toContain('marie');
    });
});