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

package io.relinkr.user.service;

import io.relinkr.user.model.UserProfile;
import io.relinkr.user.model.UserProfileType;
import java.util.Map;

/**
 * Creates {@link UserProfile} instances from attributes returned by that provider which
 * authenticated the current user.
 */
public interface UserProfileFactory {

  /**
   * Creates a {@code UserProfile}.
   *
   * @param userProfileTypeString profile type as a string, see {@link UserProfileType}
   * @param userAttributes attributes
   * @return a {@code UserProfile}
   * @throws IllegalArgumentException if {@code userProfileTypeString} is invalid
   */
  UserProfile create(String userProfileTypeString, Map<String, Object> userAttributes);

  /**
   * Creates a {@code UserProfile}.
   *
   * @param userProfileType profile type
   * @param userAttributes attributes
   * @return a {@code UserProfile}
   */
  UserProfile create(UserProfileType userProfileType, Map<String, Object> userAttributes);

}
