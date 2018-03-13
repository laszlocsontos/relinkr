package com.springuni.hermes.domain.utm;

import com.springuni.hermes.core.ApplicationException;

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
