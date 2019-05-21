package io.relinkr.core.web;

import org.springframework.core.NestedRuntimeException;

class JwsCookieManagerException extends NestedRuntimeException {

  public JwsCookieManagerException(String msg) {
    super(msg);
  }

  public JwsCookieManagerException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
