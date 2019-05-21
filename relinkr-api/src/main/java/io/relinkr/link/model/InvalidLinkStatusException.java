package io.relinkr.link.model;

import io.relinkr.core.model.ApplicationException;
import java.util.Set;

public class InvalidLinkStatusException extends ApplicationException {

  public InvalidLinkStatusException() {
  }

  public InvalidLinkStatusException(String message) {
    super(message);
  }

  public InvalidLinkStatusException(Throwable cause) {
    super(cause);
  }

  public InvalidLinkStatusException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Factory methods for creating a {@code InvalidLinkStatusException} for the specified link status
   * with communicating the expected link statuses.
   *
   * @param linkStatus That links status which was invalid to use
   * @param expectedLinkStatuses Expected statuses
   *
   * @return InvalidLinkStatusException (never {@code null}
   */
  public static InvalidLinkStatusException forLinkStatus(
      LinkStatus linkStatus, Set<LinkStatus> expectedLinkStatuses) {

    return new InvalidLinkStatusException(
        "For link status: " + linkStatus + "; expected: " + expectedLinkStatuses);
  }

}
