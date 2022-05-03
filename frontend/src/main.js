import Vue from 'vue'
import App from '@/App.vue'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import vuetify from './plugins/vuetify';
import VueResource from 'vue-resource';

Vue.use(Vuetify)
Vue.use(VueResource)
Vue.config.productionTip = false

export const eventBus = new Vue()

new Vue({
    vuetify,
    render: h => h(App)
}).$mount('#app')