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

import Vue from 'vue';
import VueCookies from 'vue-cookies';
import _ from 'lodash';
import {AUTH_TOKEN_FETCH_STRATEGY} from '../../config'
import {get} from '../../api';
import router from "../../router";

Vue.use(VueCookies);

const TOKEN_PAYLOAD_COOKIE_NAME = 'atp';

// initial state
const state = {
  auth: {
    userId: 0,
    expiresAt: 0,
    roles: []
  }
};

const getters = {
  isLoggedIn: state => {
    const clockTimestamp = Math.floor(Date.now() / 1000);
    return (clockTimestamp <= (state.auth.expiresAt || 0));
  },

  userId: state => state.auth.userId
};

const mutations = {
  setAuthentication(state, auth) {
    state.auth = _.assign(state.auth, auth || {});
  },
  clearAuthentication(state) {
    state.auth = {
      userId: 0,
      expiresAt: 0,
      roles: []
    };
    this._vm.$cookies.remove(TOKEN_PAYLOAD_COOKIE_NAME);
  }
};

const actions = {
  async checkToken({dispatch}) {
    try {
      let auth;

      const strategy = _.toLower(AUTH_TOKEN_FETCH_STRATEGY);
      switch (strategy) {
        case "cookies": {
          let token = this._vm.$cookies.get(TOKEN_PAYLOAD_COOKIE_NAME);
          if (_.isEmpty(token)) {
            return;
          }

          let decoded = JSON.parse(atob(token));

          auth = {
            userId: decoded.sub,
            expiresAt: decoded.exp
          };

          break;
        }

        case "api": {
          // When there's no authentication cookie, this HTTP call receives
          // HTTP 401 for which axios throws an exception.
          const response = await get({endpoint: '/v1/users/checkToken'});

          auth = response.data;
          break;
        }

        default: {
          throw `Invalid auth token fetch strategy: ${strategy}`;
        }
      }

      dispatch('login', auth);
    } catch (err) {
      // eslint-disable-next-line no-console
      console.log("Failed to authenticate; reason: ", err);

      dispatch('logout');
    }

  },
  login({commit}, args) {
    const auth = args || {};
    commit('setAuthentication', auth);
    router.push({path: '/dashboard'});
  },
  logout({commit}) {
    commit('clearAuthentication');
    router.push({path: '/login'});
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
