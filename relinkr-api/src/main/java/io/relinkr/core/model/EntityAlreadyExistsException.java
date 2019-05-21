package io.relinkr.core.model;

/**
 * Created by lcsontos on 5/10/17.
 */
public class EntityAlreadyExistsException extends GeneralEntityException {

  public EntityAlreadyExistsException(String fieldName, Object fieldValue) {
    super(fieldName, fieldValue);
  }

}
