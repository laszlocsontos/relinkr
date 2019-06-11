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

import _ from 'lodash';
import axios from 'axios';

import {API_BASE_URL} from './config'

const DEFAULT_HEADERS = {
  'Accept': 'application/json, application/hal+json',
  'Content-Type': 'application/json',
  'X-Requested-With': 'XMLHttpRequest'
};

const fetch = (method, endpoint, authToken, headers = {}, params = {},
    data = null) => {
  const config = {
    method: method,
    url: `${API_BASE_URL}${endpoint}`,
    params: _.defaults({}, params || {}, {bust: (Date.now())}),
    headers: _.defaults(DEFAULT_HEADERS, headers || {}),
    withCredentials: true
  };

  if (!_.isEmpty(authToken)) {
    config.headers = _.defaults(config.headers,
        {'Authorization': `Bearer ${authToken}`});
  }

  if (data) {
    config.data = data;
  }

  return axios(config);
};

export const get = ({endpoint, params, headers}) =>
    fetch('GET', endpoint, headers, params);

export const head = ({endpoint, params, headers}) =>
    fetch('HEAD', endpoint, headers, params);

export const del = ({endpoint, params, headers}) =>
    fetch('DELETE', endpoint, headers, params);

export const post = ({endpoint, data, params, headers}) =>
    fetch('POST', endpoint, headers, params, data);

export const patch = ({endpoint, data = {}, params, headers}) =>
    fetch('PATCH', endpoint, headers, params, data);

export const put = ({endpoint, data, params, headers}) =>
    fetch('PUT', endpoint, headers, params, data);
