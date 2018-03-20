package com.springuni.hermes.user;

import com.springuni.hermes.core.model.GeneralEntityException;

public class UnconfirmedUserException extends GeneralEntityException {

    public UnconfirmedUserException(String fieldName, Object fieldValue) {
        super(fieldName, fieldValue);
    }

}
