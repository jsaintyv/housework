import DateUtils from '../utils/DateUtils';


export const UserAdmin = {
    id:1, login: "admin"
};

export const UserHugo = {
    id: 2, login: "hugo"
}

export const UserMarie = {
    id: 3, login: "marie"
}

export const TypeInstallTable = {
    id: 1, name: "Install table", value: 1
};

export const TypePutInDishWashing = {
    id: 2, name: "Put in dish-washing", value: 1
}


export const SampleHouse = {
    id: 9,
    owner: UserAdmin,
    users: [
        UserHugo,
        UserMarie
    ],
    types: [
        TypeInstallTable,
        TypePutInDishWashing
    ]
};

export const DATE_2412 = DateUtils.convertInputValue("2019-12-24");
export const WORKS_2412 = [
    {
        id: 1,
        date: DateUtils.convertInputValue("2019-12-24").valueOf(),
        worker: UserHugo,
        type: TypeInstallTable
    },
    {
        id: 2,
        date: DateUtils.convertInputValue("2019-12-23").valueOf(),
        worker: UserHugo,
        type: TypeInstallTable
    },
    {
        id: 3,
        date: DateUtils.convertInputValue("2019-12-23").valueOf(),
        worker: UserMarie,
        type: TypePutInDishWashing
    }
];