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

const DEFAULT_PERIOD = 'THIS_WEEK';

// initial state
const state = {
  period: DEFAULT_PERIOD,

  linksStats: {},
  clicksStats: {},
  visitorsStats: {},

  // do not display any number until the data is fetched
  linksCount: null,
  clicksCount: null,
  visitorsCount: null
};

const getters = {
  // Pass a copy of the state to prevent "vuex store state modified outside
  // mutation handlers" error, the Chart modifies this data if there are
  // multiple charts on the page.
  getLinksStats: () => _.cloneDeep(state.linksStats),
  getClicksStats: () => _.cloneDeep(state.clicksStats),
  getVisitorsStats: () => _.cloneDeep(state.visitorsStats)
};

const mutations = {
  setStats(state, {statType, data}) {
    state.period = _.defaultTo(_.get(data, 'timespan.period'), DEFAULT_PERIOD);

    const embeddedData = _.defaultTo(_.get(data, '_embedded.data'), []);
    const dataArray = [];

    const labelsArray = [];
    let totalCount = 0;
    for (const element of embeddedData) {
      dataArray.push(element.value);
      labelsArray.push(element.key);
      totalCount += element.value;

    }
    state[`${statType}Count`] = totalCount;
    state[`${statType}Stats`] = {
      datasets: [{
        label: `# of ${statType}`,
        data: dataArray
      }],
      labels: labelsArray
    };
  }
};

const actions = {
  fetchStats({commit}, {statType, period}) {
    // statType: links/clicks/visitors
    get({endpoint: `/v1/stats/${statType}/${period}`})
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
