/*
  Copyright [2018-2019] Laszlo Csontos (sole trader)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
 
/* eslint-disable */

import Vue from 'vue'
import Vuex from 'vuex'
import VueCookies from 'vue-cookies'

Vue.use(Vuex);
Vue.use(VueCookies);

import _ from 'lodash'

import router from './router'

const TOKEN_PAYLOAD_COOKIE_NAME = 'atp';

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
    },
    clearAuthentication(state) {
      state.auth = {
        userId: 0,
        expiresAt: 0
      };
      this._vm.$cookies.remove(TOKEN_PAYLOAD_COOKIE_NAME);
    }
  },
  getters: {
    isLoggedIn: state => {
      const clockTimestamp = Math.floor(Date.now() / 1000);
      return (clockTimestamp <= (state.auth.expiresAt  || 0));
    },
    userId: state => state.auth.userId
  },
  actions: {
    checkToken({ commit, dispatch }) {
      console.log("checkToken");

      const token = this._vm.$cookies.get(TOKEN_PAYLOAD_COOKIE_NAME);
      if (_.isEmpty(token)) {
        return;
      }

      try {
        console.log("token", token);
        let decoded = JSON.parse(atob(token));
        console.log("decoded", decoded);
        dispatch('login', {
          auth: {
            userId: decoded.sub,
            expiresAt: decoded.exp
          }
        });
      } catch (err) {
        dispatch('logout');
      }
    },
    login ({ commit }, args) {
      const { auth } = args || {};
      commit('setAuthentication', auth);
      router.push({ path: '/dashboard' });
      console.log("login: ", auth);
    },
    logout ({ commit }) {
      commit('clearAuthentication');
      router.push({ path: '/login' });
      console.log("logout");
    }
  }
});
