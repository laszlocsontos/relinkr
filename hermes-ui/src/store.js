/* eslint-disable */

import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    authToken: "",
    isLoggedIn: false
  },
  mutations: {
    login(state) {
      state.isLoggedIn = true;
    },
    logout(state) {
      state.isLoggedIn = false;
    }
  },
  actions: {
    obtainAuthToken ({ commit, state }, args) {
      const { registrationId, query } = args;
      console.log(registrationId, query);
    }
  }
});
