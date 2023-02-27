import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import sendAjax from './util/networking'
import VueResource from 'vue-resource'
import interceptors from "./util/interceptors";

import VueHighlightJS from 'vue-highlightjs'
import 'highlight.js/styles/monokai-sublime.css'


Vue.use(VueHighlightJS)



Vue.prototype.$sendAjax = sendAjax;

Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.use(VueResource);

interceptors(store,router);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

