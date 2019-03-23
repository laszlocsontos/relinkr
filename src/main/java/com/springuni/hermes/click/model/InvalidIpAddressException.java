package com.springuni.hermes.click.model;

public class InvalidIpAddressException extends Exception {

    public InvalidIpAddressException() {
    }

    public InvalidIpAddressException(String message) {
        super(message);
    }

    public InvalidIpAddressException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidIpAddressException(Throwable cause) {
        super(cause);
    }

    static InvalidIpAddressException forIpAddress(String ipAddress) {
        return new InvalidIpAddressException("For IP address: " + ipAddress);
    }

}
