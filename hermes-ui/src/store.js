/* eslint-disable */

import Vue from 'vue'
import Vuex from 'vuex'
import VueCookies from 'vue-cookies'

Vue.use(Vuex);
Vue.use(VueCookies);

import jwt from 'jsonwebtoken'
import _ from 'lodash'

import { post } from './api'
import router from './router'

import { JWT_PUBLIC_KEY, OAUTH2_LOGIN_ENDPOINT } from "./config";

export default new Vuex.Store({
  state: {
    auth: {
      userId: 0,
      expiresAt: 0,
      roles: []
    }
  },
  mutations: {
    setAuthentication(state, auth) {
      state.auth = auth;
      localStorage.setItem(AUTH_TOKEN_KEY, auth.token);
    },
    clearAuthentication(state) {
      state.auth = {
        token: "",
        userId: 0,
        expiresAt: 0
      };
      localStorage.removeItem(AUTH_TOKEN_KEY);
    }
  },
  getters: {
    isLoggedIn: state => {
      const clockTimestamp = Math.floor(Date.now() / 1000);
      return (clockTimestamp <= (state.auth.expiresAt  || 0));
    },
    authToken: state => state.token
  },
  actions: {
    checkToken({ commit, dispatch }, args) {
      let { token, redirect } = args || {};
      console.log("checkToken", token, redirect);

      token = _.defaultTo(token, localStorage.getItem(AUTH_TOKEN_KEY));
      if (_.isEmpty(token)) {
        return;
      }

      try {
        console.log("token", token);
        let decoded = jwt.verify(token, JWT_PUBLIC_KEY, { algorithms: ['RS256'] });
        console.log("decoded", decoded);
        dispatch('login', {
          auth: {
            token: token,
            userId: decoded.sub,
            expiresAt: decoded.exp
          },
          redirect: redirect
        });
      } catch (err) {
        dispatch('logout', { redirect: redirect });
      }
    },
    obtainAuthToken ({ commit, dispatch }, args) {
      const { registrationId, query, redirect } = args || {};
      console.log("obtainAuthToken", registrationId, query, redirect);

      post({ endpoint: `${OAUTH2_LOGIN_ENDPOINT}/${registrationId}`, params: { ...query }})
        .then(response => dispatch('checkToken', { token: response.token, redirect: redirect }))
        .catch(err => console.log("error", err));
    },
    login ({ commit }, args) {
      const { auth, redirect } = args || {};
      commit('setAuthentication', auth);
      router.push({ path: _.defaultTo(redirect, '/dashboard') });
      console.log("login: ", auth, redirect);
    },
    logout ({ commit }, args) {
      const { redirect } = args || {};
      commit('clearAuthentication');
      router.push({ path: '/login', query: { redirect: redirect } });
      console.log("logout", redirect);
    }
  }
});
