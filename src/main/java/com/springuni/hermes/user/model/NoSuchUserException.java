package com.springuni.hermes.user.model;

import com.springuni.hermes.core.model.EntityNotFoundException;

public class NoSuchUserException extends EntityNotFoundException {

    public NoSuchUserException(String fieldName, Object fieldValue) {
        super(fieldName, fieldValue);
    }

}