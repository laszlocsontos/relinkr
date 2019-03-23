package com.springuni.hermes.click.model;

import static com.springuni.hermes.Mocks.IPV4_ADDRESS;
import static com.springuni.hermes.Mocks.IPV6_ADDRESS;
import static com.springuni.hermes.click.model.IpAddress.IpAddressType.IPV4;
import static com.springuni.hermes.click.model.IpAddress.IpAddressType.IPV6;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.springuni.hermes.click.model.InvalidIpAddressException;
import com.springuni.hermes.click.model.IpAddress;
import java.math.BigInteger;
import org.junit.Before;
import org.junit.Test;

public class IpAddressTest {

    private IpAddress ipv4Address;
    private IpAddress ipv6Address;

    @Before
    public void setUp() throws Exception {
        ipv4Address = new IpAddress(IPV4_ADDRESS);
        ipv6Address = new IpAddress(IPV6_ADDRESS);
    }

    @Test(expected = InvalidIpAddressException.class)
    public void create_withInvalid() throws Exception {
        new IpAddress("bad");
    }

    @Test
    public void getIpAddress_withIPV4() {
        assertEquals(IPV4_ADDRESS, ipv4Address.getIpAddress());
    }

    @Test
    public void getIpAddressDecimal_withIPV4() {
        assertEquals(BigInteger.valueOf(3090433715L), ipv4Address.getIpAddressDecimal());
    }

    @Test
    public void getIpAddressType_withIPV4() {
        assertEquals(IPV4, ipv4Address.getIpAddressType());
    }

    @Test
    public void isLocalAddress_withIPV4() {
        assertFalse(ipv4Address.isLocalAddress());
    }

    @Test
    public void isMulticastAddress_withIPV4() {
        assertFalse(ipv4Address.isMulticastAddress());
    }

    @Test
    public void getIpAddress_withIPV6() {
        assertEquals(IPV6_ADDRESS, ipv6Address.getIpAddress());
    }

    @Test
    public void getIpAddressDecimal_withIPV6() {
        assertEquals(new BigInteger("42540766452641154071740215577757643572"),
                ipv6Address.getIpAddressDecimal());
    }

    @Test
    public void getIpAddressType_withIPV6() {
        assertEquals(IPV6, ipv6Address.getIpAddressType());
    }

    @Test
    public void isLocalAddress_withIPV6() {
        assertFalse(ipv6Address.isLocalAddress());
    }

    @Test
    public void isMulticastAddress_withIPV6() {
        assertFalse(ipv6Address.isMulticastAddress());
    }

}
