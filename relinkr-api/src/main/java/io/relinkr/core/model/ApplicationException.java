package io.relinkr.core.model;

/**
 * Created by lcsontos on 5/10/17.
 */
public class ApplicationException extends RuntimeException {

  public ApplicationException() {
    super();
  }

  public ApplicationException(String message) {
    super(message);
  }

  public ApplicationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ApplicationException(Throwable cause) {
    super(cause);
  }

}
