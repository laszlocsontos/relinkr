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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class AsyncActionInterceptor implements MethodInterceptor {

  private final BlockingQueue<AsyncActionResult> results = new LinkedBlockingQueue<>();

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Object result = invocation.proceed();

    AsyncActionResult asyncActionResult =
        AsyncActionResult.of(invocation.getArguments(), result, Thread.currentThread());

    results.offer(asyncActionResult);

    return result;
  }

  public AsyncActionResult takeResult() {
    try {
      return results.poll(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void clearResults() {
    results.clear();
  }

}
