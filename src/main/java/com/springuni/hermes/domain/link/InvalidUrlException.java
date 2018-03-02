package com.springuni.hermes.domain.link;

public class InvalidUrlException extends Exception {

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
