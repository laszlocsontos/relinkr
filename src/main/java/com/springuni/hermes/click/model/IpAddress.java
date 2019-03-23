package com.springuni.hermes.click.model;

import java.math.BigInteger;
import java.net.Inet6Address;
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

    public IpAddress(String ipAddress) throws InvalidIpAddressException {
        Assert.notNull(ipAddress, "ipAddress cannot be null");
        this.ipAddress = ipAddress;

        if (!IPV4_AND_6_PATTERN.matcher(ipAddress).matches()) {
            throw InvalidIpAddressException.forIpAddress(ipAddress);
        }

        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            throw new InvalidIpAddressException(e);
        }

        localAddress = inetAddress.isLinkLocalAddress() || inetAddress.isSiteLocalAddress();
        multicastAddress = inetAddress.isMulticastAddress();

        if (inetAddress instanceof Inet6Address) {
            ipAddressType = IpAddressType.IPV6;
        } else {
            ipAddressType = IpAddressType.IPV4;
        }

        byte[] ipAddressBytes = inetAddress.getAddress();
        ipAddressDecimal = BigInteger.ZERO;
        for (int index = 0; index < ipAddressBytes.length; index++) {
            BigInteger value = BigInteger.valueOf(ipAddressBytes[index] & 0xFF);
            int power = ipAddressBytes.length - index - 1;
            ipAddressDecimal = ipAddressDecimal.add(B_256.pow(power).multiply(value));
        }
    }

    IpAddress() {
    }

    public enum IpAddressType {
        IPV4, IPV6
    }

}
