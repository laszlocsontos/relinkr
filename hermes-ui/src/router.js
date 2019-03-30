import Vue from 'vue'
import Router from 'vue-router'
import Dashboard from '@/views/Dashboard.vue'
import Links from '@/views/Links.vue'
import Stats from '@/views/Stats.vue'

Vue.use(Router);

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'dashboard',
      component: Dashboard
    },
    {
      path: '/links',
      name: 'links',
      component: Links
    },
    {
      path: '/stats',
      name: 'stats',
      component: Stats
    }
  ]
})
