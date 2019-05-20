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
