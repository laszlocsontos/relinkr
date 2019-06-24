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
import {get, post, put} from '../../api';
import axios from 'axios';

const PAGE_SIZE = 10;

// initial state
const state = {
  userLinkStatuses: {},
  _linksStats: {},
  _clicksStats: {},
  _visitorsStats: {},
  page: {
    size: 0,
    totalElements: 0,
    totalPages: 0,
    number: 0
  }
};

const getters = {
  getTotalRows: state => (state.page.totalElements),
  getPageSize: () => PAGE_SIZE,
  hasNextStatus: state => (id, nextStatus) => {
    const paths = state.userLinkStatuses[id];
    return _.some(paths, (path) => _.endsWith(path, nextStatus));
  },
  // Pass a copy of the state to prevent "vuex store state modified outside mutation
  // handlers" error, the Chart modifies this data if there are multiple charts on the page
  getLinksStats: () => _.cloneDeep(state._linksStats),
  getClicksStats: () => _.cloneDeep(state._clicksStats),
  getVisitorsStats: () => _.cloneDeep(state._visitorsStats)
};

const mutations = {
  setLinks(state, {data, callback}) {
    state.page = _.assign(state.page, data.page || {});

    const links = _.get(data, "_embedded.links", []);

    _.forEach(links, link => link.utmParameters = link.utmParameters || {});

    callback(links);

    state.userLinkStatuses = _.reduce(
        _.map(links, link => {
          let hrefs =
              _.defaultTo(_.get(link, '_links.userLinkStatuses.href'), []);

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
  },
  setStats(state, {statType, data}) {
    const embeddedData = _.defaultTo(_.get(data, '_embedded.data'), []);
    const dataArray = [];
    const labelsArray = [];

    for(const element of embeddedData) {
      dataArray.push(element.value);
      labelsArray.push(element.key);
    }

    state[`_${statType}Stats`] = {
      datasets: [{
        label: `# of ${statType}`,
        data: dataArray
      }],
      labels: labelsArray
    };
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
      commit('setLinks', {data: response.data, callback: callback});
    })
    .catch(err => {
      console.log("error", err);
      commit('setLinks', {data: {}, callback: callback})
    });
  },
  setNextStatus(_, args) {
    const {id, nextStatus, refreshCallback} = args || {
      id: 0, nextStatus: "", refreshCallback: () => {
      }
    };

    put({endpoint: `/v1/links/${id}/linkStatuses/${nextStatus}`})
    .then(() => refreshCallback())
    .catch(err => {
      console.log("error", err);
    });
  },
  saveLink(_, args) {
    const {link, successCallback, failureCallback} = args;
    post({endpoint: "/v1/links", data: link})
    .then(successCallback)
    .catch(failureCallback);
  },
  fetchStats({commit}, statType) {
    // statType: links/clicks/visitors
    axios({method: 'GET', url: `/mocks/${statType}/1`})
    .then(response => {
      commit(`setStats`, {statType: statType, data: response.data});
    })
    .catch(err => {
      console.log("error", err);
    });
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
