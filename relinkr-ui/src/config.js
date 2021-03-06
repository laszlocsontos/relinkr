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

const API_BASE_URL = process.env.VUE_APP_API_BASE_URL;
const UI_BASE_PATH = process.env.VUE_APP_UI_BASE_PATH;
const AUTH_TOKEN_FETCH_STRATEGY = process.env.VUE_APP_AUTH_TOKEN_FETCH_STRATEGY;
const OAUTH2_INIT_ENDPOINT = process.env.VUE_APP_OAUTH2_INIT_ENDPOINT;

export {
  API_BASE_URL,
  UI_BASE_PATH,
  AUTH_TOKEN_FETCH_STRATEGY,
  OAUTH2_INIT_ENDPOINT
}
