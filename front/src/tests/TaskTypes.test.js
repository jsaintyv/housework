import { mount, createLocalVue } from '@vue/test-utils';
import BootstrapVue from 'bootstrap-vue'
import { default as store,
         SELECT_HOUSE } from '../stores/index.js';
import TaskTypes from '../components/TaskTypes.vue';
import { SampleHouse } from './TestFactory.js';


// create an extended `Vue` constructor
const localVue = createLocalVue()

// install plugins as normal
localVue.use(BootstrapVue)



describe('TaskTypes', () => {
    // Now mount the component and you have the wrapper
    const wrapper = mount(TaskTypes, {localVue});

    it('no house no button', () => {
         expect(wrapper.contains('button')).toBe(false);
         store.commit(SELECT_HOUSE, SampleHouse);
         expect(wrapper.text()).toContain('Install table');
         expect(wrapper.text()).toContain('Put in dish-washing');
         expect(wrapper.contains('button')).toBe(true);
    });
});