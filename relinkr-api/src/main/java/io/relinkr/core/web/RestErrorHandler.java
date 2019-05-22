/*
  Copyright [2018-2019] Laszlo Csontos (sole trader)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
 
package io.relinkr.core.web;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

import io.relinkr.core.model.ApplicationException;
import io.relinkr.core.model.EntityAlreadyExistsException;
import io.relinkr.core.model.EntityConflictsException;
import io.relinkr.core.model.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

/**
 * Created by lcsontos on 5/10/17.
 */
@Slf4j
@RestControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<Object> handleApplicationException(final ApplicationException ex) {
    return handleExceptionInternal(ex, BAD_REQUEST);
  }

  @ExceptionHandler(EntityAlreadyExistsException.class)
  public ResponseEntity<Object> handleEntityExistsException(
      final EntityAlreadyExistsException ex) {
    return handleExceptionInternal(ex, BAD_REQUEST);
  }

  @ExceptionHandler(EntityConflictsException.class)
  public ResponseEntity<Object> handleEntityConflictsException(
      final EntityConflictsException ex) {
    return handleExceptionInternal(ex, CONFLICT);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException ex) {
    return handleExceptionInternal(ex, NOT_FOUND);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handleAccessDeniedException(final AccessDeniedException ex) {
    return handleExceptionInternal(ex, FORBIDDEN);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleException(final Exception ex) {
    log.error(ex.getMessage(), ex);
    return handleExceptionInternal(ex, INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(UnsupportedOperationException.class)
  public ResponseEntity<Object> handleUnsupportedOperationException(
      final UnsupportedOperationException ex) {

    return handleExceptionInternal(ex, NOT_IMPLEMENTED);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

    // Request may be null in test cases
    if (request != null && HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
      request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
    }

    return new ResponseEntity<>(RestErrorResponse.of(status, ex), headers, status);
  }

  private ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpStatus status) {
    return handleExceptionInternal(ex, null, null, status, null);
  }

}
