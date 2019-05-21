package io.relinkr.link.model;

import io.relinkr.core.model.ApplicationException;

public class InvalidUrlException extends ApplicationException {

  public InvalidUrlException() {
  }

  public InvalidUrlException(String message) {
    super(message);
  }

  public InvalidUrlException(Throwable cause) {
    super(cause);
  }

  public InvalidUrlException(String message, Throwable cause) {
    super(message, cause);
  }

}
