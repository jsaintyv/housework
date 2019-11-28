import DateUtils from '../utils/DateUtils.js';

describe('DateUtils', () => {
    it('test build7DaysBefore', () => {
        var d = DateUtils.convertInputValue("2019-12-24");
        expect(d.getDate()).toBe(24);

        var days7 = DateUtils.build7DayBefore(d);
        expect(days7.length).toBe(7);

        expect(days7[0].getDate()).toBe(18);
        expect(days7[6].getDate()).toBe(24);

        d = DateUtils.convertInputValue("2019-11-01");
        days7 = DateUtils.build7DayBefore(d);
        expect(days7.length).toBe(7);

        expect(days7[0].getDate()).toBe(26);
        expect(days7[6].getDate()).toBe(1);
    }); 
});