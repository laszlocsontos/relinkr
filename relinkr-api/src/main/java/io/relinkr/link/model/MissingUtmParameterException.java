package io.relinkr.link.model;

import io.relinkr.core.model.ApplicationException;

public class MissingUtmParameterException extends ApplicationException {

  public MissingUtmParameterException() {
    super();
  }

  public MissingUtmParameterException(String message) {
    super(message);
  }

  static MissingUtmParameterException forUtmParameter(String name) {
    return new MissingUtmParameterException("For UTM parameter: " + name);
  }

}
