package com.springuni.hermes.domain.link;

import com.springuni.hermes.core.ApplicationException;

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

    static InvalidLinkStatusException forLinkStatus(LinkStatus linkStatus) {
        return new InvalidLinkStatusException("For link status: " + linkStatus);
    }

}
