import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../layouts/Layout.vue'),
    children:[
      {
        path : 'sync/index',
        name: 'Index',
        component: () => import('../views/sync/Welcome')
      },
      {
        path : 'sync/plan',
        name: 'Plan',
        component: () => import('../views/sync/Plan')
      },
      {
        path : 'sync/result',
        name: 'Message',
        component: () => import('../views/sync/Result')
      },
      {
        path : 'sync/message',
        name: 'Plan',
        component: () => import('../views/sync/Message')
      },
      {
        path : 'sync/callback',
        name: 'Callback',
        component: () => import('../views/sync/Callback')
      }
    ]
  }
]

const router = new VueRouter({
  routes
})

// 解决 NavigationDuplicated: Avoided redundant navigation to current location 报错
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

export default router
