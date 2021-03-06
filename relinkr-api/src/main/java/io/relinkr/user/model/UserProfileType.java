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

package io.relinkr.user.model;

/**
 * {@link User}'s profile type, {@code NATIVE} means that the user has registered on their own and
 * filled their profile details out themselves.
 * TODO: Implement registration and add support for native profiles.
 */
public enum UserProfileType {
  FACEBOOK, GOOGLE, NATIVE;
}
