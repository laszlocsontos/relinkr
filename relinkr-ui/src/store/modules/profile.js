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

/* eslint-disable no-console */

import _ from 'lodash';
import {get} from '../../api';

// initial state
const state = {
  userProfileType: "",
  userProfileId: "",
  fullName: "",
  givenName: "",
  profileUrl: "",
  pictureUrl: ""
};

const getters = {
  profileTitle() {
    const owner = state.givenName ? state.givenName + "'s" : 'User';
    return owner + ' Profile';
  }
};

const mutations = {
  setProfile(state, data) {
    state = _.assign(state, data.userProfile || {});
  }
};

const actions = {
  fetchProfile({commit, rootGetters}) {
    const userId = rootGetters['auth/userId'];
    get({endpoint: `/v1/users/${userId}`})
    .then(response => commit('setProfile', response.data))
    .catch(err => console.log("error", err));
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
