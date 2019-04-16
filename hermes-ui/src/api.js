import _ from 'lodash';
import axios from 'axios';

import { API_BASE_URL } from './config'

const DEFAULT_HEADERS = {
  'Accept': 'application/json',
  'Content-Type': 'application/json'
};

const fetch = (method, endpoint, authToken, headers = {}, params = {}, data=null) => {
  const config = {
    method: method,
    url: `${API_BASE_URL}/${endpoint}`,
    params: _.defaults({}, params || {}, { bust: (Date.now()) }),
    headers: _.defaults(DEFAULT_HEADERS, headers || {})
  };

  if (!_.isEmpty(authToken)) {
    config.headers = _.defaults(config.headers, { 'Authorization': `Bearer ${authToken}` });
  }

  if (data) {
    config.data = data;
  }

  console.log("opts", config);
  return axios(config);
};

export const get = ({ endpoint, authToken, params, headers }) =>
    fetch('GET', endpoint, authToken, headers, params);

export const head = ({ endpoint, authToken, params, headers }) =>
    fetch('HEAD', endpoint, authToken, headers, params);

export const del = ({ endpoint, authToken, params, headers }) =>
    fetch('DELETE', endpoint, authToken, headers, params);

export const post = ({ endpoint, authToken, data, params, headers }) =>
    fetch('POST', endpoint, authToken, headers, params, data);

export const patch = ({ endpoint, authToken, data={}, params, headers }) =>
    fetch('PATCH', endpoint, authToken, headers, params, data);

export const put = ({ endpoint, authToken, data, params, headers }) =>
    fetch('PUT', endpoint, authToken, headers, params, data);
