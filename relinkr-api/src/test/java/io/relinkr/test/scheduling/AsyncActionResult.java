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
