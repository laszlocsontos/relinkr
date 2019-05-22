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

package io.relinkr.test.scheduling;

import java.util.Optional;
import lombok.NonNull;

public class AsyncActionResult {

  private final Object[] arguments;
  private final Object result;
  private final Thread executor;

  private AsyncActionResult(Object[] arguments, Object result, Thread executor) {
    this.arguments = Optional.ofNullable(arguments).orElse(new Object[0]);
    this.result = result;
    this.executor = executor;
  }

  public static AsyncActionResult of(
      Object[] arguments, Object result, @NonNull Thread executor) {

    return new AsyncActionResult(arguments, result, executor);
  }

  public Object[] getArguments() {
    return arguments;
  }

  public Optional<?> getResult() {
    return Optional.ofNullable(result);
  }

  public String getExecutorName() {
    return executor.getName();
  }

}
