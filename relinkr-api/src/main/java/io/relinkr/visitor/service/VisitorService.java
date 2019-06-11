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

package io.relinkr.visitor.service;

import io.relinkr.visitor.model.VisitorId;

/**
 * Provides the service layer for {@link io.relinkr.visitor.model.Visitor}s. The only operation that
 * is supported right now is {@link VisitorService#ensureVisitor(VisitorId)}, see below.
 */
public interface VisitorService {

  /**
   * Ensures that a {@code VisitorId} exists.
   *
   * @param visitorId Visitor's ID to check
   * @return {@code visitorId} if it's exists or a new ID if it didn't exist
   */
  VisitorId ensureVisitor(VisitorId visitorId);

}
