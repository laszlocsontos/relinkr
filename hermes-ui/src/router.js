import Vue from 'vue'
import Router from 'vue-router'

import store from './store.js';

import Login from '@/views/Login.vue'
import Dashboard from '@/views/Dashboard.vue'
import Links from '@/views/Links.vue'
import Stats from '@/views/Stats.vue'

Vue.use(Router);

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '*',
      redirect: '/dashboard'
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: Dashboard
    },
    {
      path: '/login',
      name: 'login',
      component: Login
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
});

router.beforeEach((to, from, next) => {
  const publicPages = ['/login'];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = store.state.isLoggedIn;

  // eslint-disable-next-line
  console.log("loggedIn", loggedIn);

  if (authRequired && !loggedIn) {
    return next('/login');
  }

  next();
});

export default router;
