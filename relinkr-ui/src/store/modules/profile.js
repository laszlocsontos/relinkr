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
  userProfile: {
    userProfileType: "",
    userProfileId: "",
    fullName: "",
    profileUrl: "",
    pictureUrl: ""
  }
};

const getters = {};

const mutations = {
  setProfile(state, data) {
    console.log("setProfile", data);
    state.userProfile = _.assign(state.userProfile, data.userProfile || {});
  }
};

const actions = {
  fetchProfile({commit}, args) {
    const {userId} = args || {};
    console.log("fetchProfile", userId);

    get({endpoint: `/v1/users/${userId}`})
    .then(response => commit('setProfile', response.data))
    .catch(err => console.log("error", err));
  }
};

export default {
  state,
  getters,
  mutations,
  actions
};
