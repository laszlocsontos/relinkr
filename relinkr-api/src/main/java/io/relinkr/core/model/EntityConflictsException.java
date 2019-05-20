package io.relinkr.core.model;

/**
 * Created by lcsontos on 5/10/17.
 */
public class EntityConflictsException extends GeneralEntityException {

    public EntityConflictsException(String fieldName, Object fieldValue) {
        super(fieldName, fieldValue);
    }

}
