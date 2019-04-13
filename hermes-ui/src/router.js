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
      path: '/oauth2/callback/:registrationId',
      name: 'oauth2-callback',
      meta: { oauth2Callback: true }
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: Dashboard,
      meta: { requiresAuth: true }
    },
    {
      path: '/links',
      name: 'links',
      component: Links,
      meta: { requiresAuth: true }
    },
    {
      path: '/stats',
      name: 'stats',
      component: Stats,
      meta: { requiresAuth: true }
    }
  ]
});

router.beforeEach((to, from, next) => {
  // This route requires auth, check if logged in if not, redirect to login page.
  if (to.meta.requiresAuth) {
    if (!store.state.isLoggedIn) {
      next({ path: '/login', query: { redirect: to.fullPath } });
    } else {
      next();
    }
  // This route processes OAuth2 callbacks, obtain auth token with the back-end.
  } else if (to.meta.oauth2Callback) {
    store.dispatch(
        'obtainAuthToken',
        { registrationId: to.params.registrationId, query: to.query }
    );
  } else {
    next();
  }
});

export default router;
