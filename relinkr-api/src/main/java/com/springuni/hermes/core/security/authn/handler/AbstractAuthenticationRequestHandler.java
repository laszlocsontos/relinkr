package com.springuni.hermes.core.security.authn.handler;


import static lombok.AccessLevel.PACKAGE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.springuni.hermes.core.web.RestErrorResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = PACKAGE)
class AbstractAuthenticationRequestHandler {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ObjectWriter objectWriter;

    void handle(HttpServletResponse response, HttpStatus httpStatus, Object valueToWrite)
            throws IOException {

        doWrite(response, httpStatus, valueToWrite);
    }

    void handleError(HttpServletResponse response, Exception e) throws IOException {
        doWrite(response, INTERNAL_SERVER_ERROR, RestErrorResponse.of(INTERNAL_SERVER_ERROR, e));
    }

    private void doWrite(
            HttpServletResponse response, HttpStatus httpStatus, Object valueToWrite)
            throws IOException {

        if (response.isCommitted()) {
            log.debug("Response has already been committed, cannot set HTTP status.");
            return;
        }

        response.setStatus(httpStatus.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        objectWriter.writeValue(response.getWriter(), valueToWrite);
    }

}
