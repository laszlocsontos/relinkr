package io.relinkr.core.model;

/**
 * Created by lcsontos on 5/10/17.
 */
public class EntityNotFoundException extends GeneralEntityException {

  public EntityNotFoundException(String fieldName, Object fieldValue) {
    super(fieldName, fieldValue);
  }

}
