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
