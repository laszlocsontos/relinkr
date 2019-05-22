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
 
package io.relinkr.click.model;

import static io.relinkr.test.Mocks.IPV4_ADDRESS;
import static io.relinkr.test.Mocks.IPV6_ADDRESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import io.relinkr.click.model.IpAddress.IpAddressType;
import java.math.BigInteger;
import org.junit.Before;
import org.junit.Test;

public class IpAddressTest {

  private IpAddress ipv4Address;
  private IpAddress ipv6Address;

  @Before
  public void setUp() {
    ipv4Address = IpAddress.fromString(IPV4_ADDRESS);
    ipv6Address = IpAddress.fromString(IPV6_ADDRESS);
  }

  @Test(expected = InvalidIpAddressException.class)
  public void create_withInvalid() {
    IpAddress.fromString("bad");
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
    assertEquals(IpAddressType.IPV4, ipv4Address.getIpAddressType());
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
    assertEquals(IpAddressType.IPV6, ipv6Address.getIpAddressType());
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
