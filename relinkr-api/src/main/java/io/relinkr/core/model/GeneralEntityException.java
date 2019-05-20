package io.relinkr.core.model;

import lombok.Getter;

@Getter
public class GeneralEntityException extends ApplicationException {

    private final String fieldName;
    private final Object fieldValue;

    public GeneralEntityException(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}
