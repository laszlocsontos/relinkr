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
import {get, put} from '../../api';

const PAGE_SIZE = 10;

// initial state
const state = {
  userLinkStatuses: {},
  page: {
    size: 0,
    totalElements: 0,
    totalPages: 0,
    number: 0
  }
};

const getters = {
  totalRows: state => (state.page.totalElements),
  perPage: () => PAGE_SIZE,
  hasNextStatus: state => (id, nextStatus) => {
    const paths = state.userLinkStatuses[id];
    console.log("hasNextStatus", id, nextStatus, paths);
    return _.some(paths, (path) => _.endsWith(path, nextStatus));
  }
};

const mutations = {
  setState(state, {data, callback}) {
    state.page = _.assign(state.page, data.page || {});

    const links = _.defaultTo(_.get(data, "_embedded.links"), []);
    callback(links);

    state.userLinkStatuses = _.reduce(
        _.map(links, link => {
          let hrefs =
              _.defaultTo(_.get(link, "_links.userLinkStatuses.href"), []);

          if (!_.isArray(hrefs)) {
            hrefs = [hrefs];
          }

          const paths = _.map(hrefs, href => new URL(href).pathname);
          return {id: link.id, paths: paths};
        }),
        (result, value) => {
          result[value.id] = value.paths;
          return result;
        },
        {}
    );
  }
};

const actions = {
  fetchLinks({commit}, args) {
    const {page, callback} = args || {
      page: 0, callback: () => {
      }
    };

    get({endpoint: "/v1/links", params: {page: (page - 1), size: PAGE_SIZE}})
    .then(response => {
      commit('setState', {data: response.data, callback: callback});
    })
    .catch(err => {
      console.log("error", err);
      commit('setState', {data: {}, callback: callback})
    });
  },
  setNextStatus(_, args) {
    const {id, nextStatus, refreshCallback} = args || {
      id: 0, nextStatus: "", refreshCallback: () => { }
    };

    put({endpoint: `/v1/links/${id}/linkStatuses/${nextStatus}`})
    .then(() => refreshCallback())
    .catch(err => {
      console.log("error", err);
    });
  },

};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
