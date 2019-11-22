<template>
    <div v-if="getSelected != null">
        <div>Workers</div>
        <b-row v-for="u in getSelected.users" v-bind:key="u.id">
            <b-col>
                {{u.login}}
            </b-col>
            <b-col><b-button @click="remove(u)">-</b-button></b-col>
        </b-row>
        <b-row>
            <b-col>
                <b-form-input id="input-" v-model="newWorker" type="text" required></b-form-input>
            </b-col>
            <b-col>
                <b-button @click="register()">+</b-button>
            </b-col>
        </b-row>
    </div>
</template>

<script>
    import {
        default as store
    } from "../stores/index.js";
    import lang from "../lang.js";
    import HouseServices from "../services/HouseService.js";

    export default {
        name: 'WorkersList',
        data() {
            var model = {
                lang: lang,
                newWorker: ""
            };
            return model;
        },
        computed: {
            getSelected() {
                return store.state.selectedHouse;
            }
        },
        methods: {
            register() {
                HouseServices
                    .invitUser(store.state.selectedHouse, this.newWorker)
                    .done(()=> {
                        this.$bvToast.toast('Utilisateur invité', {
                            variant: 'success',
                            solid: true
                        });
                    })
                    .fail(()=> {
                        this.$bvToast.toast('Utilisateur non trouvé', {
                            variant: 'danger',
                            solid: true
                        });
                    });
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>