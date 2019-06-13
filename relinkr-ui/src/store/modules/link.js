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

const PAGE_SIZE = 10;

const mockStats = {
  linksStats: {
    datasets: [{
      label: "# of links",
      data: [1, 2, 3, 2, 4, 2, 6]
    }],
    labels: ['2018-03-06', '2018-03-07', '2018-03-08', '2018-03-09', '2018-03-10',
      '2018-03-11', '2018-03-12'],
  },
  clicksStats: {
    datasets: [{
      label: "# of clicks",
      data: [10, 20, 35, 20, 42, 25, 80]
    }],
    labels: ['2018-03-06', '2018-03-07', '2018-03-08', '2018-03-09', '2018-03-10',
      '2018-03-11', '2018-03-12'],
  },
  visitorsStats: {
    datasets: [{
      data: [10, 30]
    }],

    // These labels appear in the legend and in the tooltips when hovering different arcs
    labels: [
      'New',
      'Returning'
    ]
  }
};

// initial state
const state = {
  userLinkStatuses: {},
  linksStats: {},
  clicksStats: {},
  visitorsStats: {},
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
    return _.some(paths, (path) => _.endsWith(path, nextStatus));
  }
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
  },
  setStats(state, {statType, data}) {
    state[`${statType}Stats`] = data;
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
    // TODO: request data and transform it
    const data = mockStats[`${statType}Stats`];
    commit('setStats', {statType: statType, data: data});
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
