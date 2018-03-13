package com.springuni.hermes.link;

import com.springuni.hermes.core.ApplicationException;

public class UnsupportedLinkOperationException extends ApplicationException {

    public UnsupportedLinkOperationException() {
    }

    public UnsupportedLinkOperationException(String message) {
        super(message);
    }

    public UnsupportedLinkOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedLinkOperationException(Throwable cause) {
        super(cause);
    }

    static UnsupportedLinkOperationException forLinkType(LinkType linkType) {
        return new UnsupportedLinkOperationException("For link type: " + linkType);
    }

}
