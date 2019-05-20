package io.relinkr.click.model;

import org.springframework.core.NestedRuntimeException;

public class InvalidIpAddressException extends NestedRuntimeException {

    public InvalidIpAddressException(String message) {
        super(message);
    }

    public InvalidIpAddressException(String message, Throwable cause) {
        super(message, cause);
    }

    static InvalidIpAddressException forIpAddress(String ipAddress) {
        return new InvalidIpAddressException("For IP address: " + ipAddress);
    }

}
