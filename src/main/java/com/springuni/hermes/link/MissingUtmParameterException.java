package com.springuni.hermes.link;

public class MissingUtmParameterException extends Exception {

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
