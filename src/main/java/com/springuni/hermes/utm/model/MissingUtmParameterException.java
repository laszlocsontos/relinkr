package com.springuni.hermes.utm.model;

import com.springuni.hermes.core.model.ApplicationException;

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
