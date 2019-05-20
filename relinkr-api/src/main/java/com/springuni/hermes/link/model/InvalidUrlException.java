package com.springuni.hermes.link.model;

import com.springuni.hermes.core.model.ApplicationException;

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
