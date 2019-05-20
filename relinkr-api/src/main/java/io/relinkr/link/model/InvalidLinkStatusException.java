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

    public static InvalidLinkStatusException forLinkStatus(
            LinkStatus linkStatus, Set<LinkStatus> expectedLinkStatuses) {

        return new InvalidLinkStatusException(
                "For link status: " + linkStatus + "; expected: " + expectedLinkStatuses);
    }

}
