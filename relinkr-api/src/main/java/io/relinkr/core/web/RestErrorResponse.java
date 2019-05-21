package io.relinkr.core.web;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

/**
 * Created by lcsontos on 5/10/17.
 */
@Getter
@NoArgsConstructor
public class RestErrorResponse {

  private int statusCode;
  private String reasonPhrase;
  private String detailMessage;

  private RestErrorResponse(HttpStatus status, String detailMessage) {
    statusCode = status.value();
    reasonPhrase = status.getReasonPhrase();
    this.detailMessage = detailMessage;
  }

  public static RestErrorResponse of(HttpStatus status, @NonNull Exception ex) {
    return new RestErrorResponse(status, ex.getMessage());
  }

}
