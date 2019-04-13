import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    isLoggedIn: false
  },
  mutations: {
    login(state) {
      state.isLoggedIn = true;
      // eslint-disable-next-line
      console.log("state", state);
    },
    logout(state) {
      state.isLoggedIn = false;
      // eslint-disable-next-line
      console.log("state", state);
    }
  },
  actions: {

  }
})
