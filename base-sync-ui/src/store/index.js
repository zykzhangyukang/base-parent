import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    editableTabsValue: '/sync/index',
    editableTabs:[
      {
        title: '系统首页',
        name: '/sync/index',
        icon: 'el-icon-s-home',
        close: false,
      }
    ]
  },
  mutations: {
    addTabs(state,tab){
      if(state.editableTabs.findIndex(e=>e.name===tab.path)===-1){
        state.editableTabs.push({
          title:tab.name,
          name:tab.path,
          icon: tab.icon,
          close:true
        })
      }
      state.editableTabsValue=tab.path
    },

    changeTabs(state,name){
      state.editableTabsValue = name;
    }
  },
  actions: {
  },
  modules: {
  }
})
