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

const PAGE_SIZE = 10;

// initial state
const state = {
  links: [{
    id: 0,
    longUrl: "",
    shortLink: {
      href: ""
    },
    createdDate: "",
    utmParameters: {
      utmSource: "",
      utmMedium: "",
      utmCampaign: "",
      utmTerm: "",
      utmContent: ""
    }
  }],
  page: {
    size: 0,
    totalElements: 0,
    totalPages: 0,
    number: 0
  }
};

const getters = {
};

const mutations = {
  setState(state, data) {
    state.page =  _.assign(state.page, data.page || {});
    state.links = _.defaultTo(data.links, []);
    console.log("setState", state);
  }
};

const actions = {
  fetchLinks({commit}, args) {
    const {page} = args || {page: 0};
    get({endpoint: "v1/links", params: {page: (page - 1), size: PAGE_SIZE}})
      .then(response => commit('setState', response.data))
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
