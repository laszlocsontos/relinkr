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

package io.relinkr.core.model;

/**
 * Should be thrown in that case when an entity with the given identifier has already been modified,
 * while the current one had been checked out for being modified.
 */
public class EntityConflictsException extends GeneralEntityException {

  public EntityConflictsException(String fieldName, Object fieldValue) {
    super(fieldName, fieldValue);
  }

}
