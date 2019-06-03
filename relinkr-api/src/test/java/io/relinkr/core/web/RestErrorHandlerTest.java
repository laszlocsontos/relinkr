package io.relinkr.core.web;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import io.relinkr.core.model.EntityAlreadyExistsException;
import io.relinkr.core.model.EntityConflictsException;
import io.relinkr.core.model.EntityNotFoundException;
import io.relinkr.link.model.InvalidUrlException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

public class RestErrorHandlerTest {

  private MockMvc mockMvc;

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(TestController.class)
        .setControllerAdvice(new RestErrorHandler())
        .build();
  }

  @Test
  public void givenApplicationException_thenBadRequest() {
    assertHttpStatus(BAD_REQUEST, "/ApplicationException");
  }

  @Test
  public void givenEntityAlreadyExistsException_thenBadRequest() {
    assertHttpStatus(BAD_REQUEST, "/EntityAlreadyExistsException");
  }

  @Test
  public void givenEntityConflictsException_thenConflict() {
    assertHttpStatus(CONFLICT, "/EntityConflictsException");
  }

  @Test
  public void givenEntityNotFoundException_thenNotFound() {
    assertHttpStatus(NOT_FOUND, "/EntityNotFoundException");
  }

  @Test
  public void givenAccessDeniedException_thenForbidden() {
    assertHttpStatus(FORBIDDEN, "/AccessDeniedException");
  }

  @Test
  public void givenGenericException_thenInternalServerError() {
    assertHttpStatus(INTERNAL_SERVER_ERROR, "/GenericException");
  }

  @Test
  public void givenUnsupportedOperationException_thenNotImplemented() {
    assertHttpStatus(NOT_IMPLEMENTED, "/UnsupportedOperationException");
  }

  private void assertHttpStatus(HttpStatus expectedStatus, String path) {
    try {
      mockMvc.perform(get(path))
          .andDo(print())
          .andExpect(result ->
              assertEquals("Status", expectedStatus.value(), result.getResponse().getStatus())
          );
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @RestController
  static class TestController {

    @GetMapping("/ApplicationException")
    public void throwsApplicationException() {
      throw new InvalidUrlException();
    }

    @GetMapping("/EntityAlreadyExistsException")
    public void throwsEntityAlreadyExistsException() {
      throw new EntityAlreadyExistsException("test", "test");
    }

    @GetMapping("/EntityConflictsException")
    public void throwsEntityConflictsException() {
      throw new EntityConflictsException("test", "test");
    }

    @GetMapping("/EntityNotFoundException")
    public void throwsEntityNotFoundException() {
      throw new EntityNotFoundException("test", "test");
    }

    @GetMapping("/AccessDeniedException")
    public void throwsAccessDeniedException() {
      throw new AccessDeniedException("access denied");
    }

    @GetMapping("/UnsupportedOperationException")
    public void throwsUnsupportedOperationException() {
      throw new UnsupportedOperationException();
    }

    @GetMapping("/GenericException")
    public void throwsGenericException() throws Exception {
      throw new Exception();
    }

  }

}
