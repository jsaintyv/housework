
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