package com.springuni.hermes.domain.link;

public class InvalidLongUrlException extends Exception {

    public InvalidLongUrlException() {
    }

    public InvalidLongUrlException(String message) {
        super(message);
    }

    public InvalidLongUrlException(Throwable cause) {
        super(cause);
    }

    public InvalidLongUrlException(String message, Throwable cause) {
        super(message, cause);
    }

}
