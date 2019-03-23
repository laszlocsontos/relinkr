package com.springuni.hermes.click.model;

import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
@Embeddable
public class IpAddress {

    private static final BigInteger B_256 = BigInteger.valueOf(256L);

    // Preliminary regex match is to avoid hostname lookups when this class is constructed with
    // a non-numeric value.
    private static final Pattern IPV4_AND_6_PATTERN =
            Pattern.compile("([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4}|(\\d{1,3}\\.){3}\\d{1,3}");

    private String ipAddress;
    private BigInteger ipAddressDecimal;

    @Enumerated
    private IpAddressType ipAddressType;
    private boolean localAddress;

    private boolean multicastAddress;
    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */

    IpAddress() {
    }

    private IpAddress(
            String ipAddress, BigInteger ipAddressDecimal, IpAddressType ipAddressType,
            boolean localAddress, boolean multicastAddress) {

        this.ipAddress = ipAddress;
        this.ipAddressDecimal = ipAddressDecimal;
        this.ipAddressType = ipAddressType;
        this.localAddress = localAddress;
        this.multicastAddress = multicastAddress;
    }

    public static IpAddress fromString(String ipAddress) throws InvalidIpAddressException {
        Assert.notNull(ipAddress, "ipAddress cannot be null");

        if (!IPV4_AND_6_PATTERN.matcher(ipAddress).matches()) {
            throw InvalidIpAddressException.forIpAddress(ipAddress);
        }

        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            throw new InvalidIpAddressException(e.getMessage(), e);
        }

        IpAddressType ipAddressType = IpAddressType.of(inetAddress);

        byte[] ipAddressBytes = inetAddress.getAddress();
        BigInteger ipAddressDecimal = ZERO;
        for (int index = 0; index < ipAddressBytes.length; index++) {
            BigInteger value = BigInteger.valueOf(ipAddressBytes[index] & 0xFF);
            int power = ipAddressBytes.length - index - 1;
            ipAddressDecimal = ipAddressDecimal.add(B_256.pow(power).multiply(value));
        }

        boolean localAddress = inetAddress.isLinkLocalAddress() || inetAddress.isSiteLocalAddress();
        boolean multicastAddress = inetAddress.isMulticastAddress();

        return new IpAddress(
                ipAddress, ipAddressDecimal, ipAddressType, localAddress, multicastAddress
        );
    }

    public enum IpAddressType {

        IPV4, IPV6;

        final static int IPV6_ADDRESS_SIZE = 16;

        static IpAddressType of(InetAddress inetAddress) {
            byte[] address = inetAddress.getAddress();
            if (address.length == IPV6_ADDRESS_SIZE) {
                return IPV6;
            }

            return IPV4;
        }

    }

}
