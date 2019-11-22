

class DateUtils {
    constructor() {
    }

    convertInputValue(v) {
        return  new Date(v + "T00:00")
    }

    addDay(d, nbDay) {
        var nd = new Date(d.valueOf());
        nd.setDate(nd.getDate()+nbDay);
        return nd;
    }
}

var instance = new DateUtils();
export default instance;