/* eslint-disable */

import Vue from 'vue'
import Vuex from 'vuex'

import router from './router'
import jwt from 'jsonwebtoken'
import _ from 'lodash'

import { JWT_PUBLIC_KEY } from "./config";

Vue.use(Vuex);

// Temporarily set add a JWT auth token to local storage
const AUTH_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI1MzI0NTM0NTM0NTM0NSIsImV4cCI6OTIyMzM3MjAzNjg1NDc3NSwiaWF0IjoxNTU1MDYwMDY1LCJqdGkiOiI1OTk5MjcyNjYwMTQ4NiIsImF1dGhvcml0aWVzIjoiVVNFUiJ9.qR-9dUgcae_voPS0HDIzGE1aKiIEY6uLkNoZLAmjhuYyte7wdtGZ3uwfDScebZkyG3BoZiRhx-IDfRJJ18Pv_iFRW-yXR6Oau3U74KLfJQnCshArSKZtHPLwNLKpS3H6dSIaWZEGi0VI2YHGhM7gpbicbjFzCfEurYT6WilLliAhyhx5NXfVOUfxqe-LZoYPFj6Bkk49Q5mxm1hjAIyxgKnYJuyaN_-46kF63JeBjoMChmUD7P4Q3NNCQVAmvmYcdUlRef22ETj-k52nGRQ1HmtXRP6GQzci6HQoGP4RAZzH4tx6kDLKVX2nzCqv8o5FxYztN8CYE9e6vYdaICJCZg";
const AUTH_TOKEN_KEY = "auth_token";

export default new Vuex.Store({
  state: {
    auth: {
      token: "",
      userId: 0,
      expiresAt: 0
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
    }
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

      // Simulate OAuth2 Login
      setTimeout(() => {
        dispatch('checkToken', { token: AUTH_TOKEN, redirect: redirect })
      }, 100);
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
