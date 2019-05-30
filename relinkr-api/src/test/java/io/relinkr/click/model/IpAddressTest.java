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

import static io.relinkr.click.model.IpAddress.IpAddressType.IPV4;
import static io.relinkr.click.model.IpAddress.IpAddressType.IPV6;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IpAddressTest {

  private static final String IPV4_ADDRESS = "184.52.70.179";
  private static final String IPV4_ADDRESS_ANON = "184.52.70.0";

  private static final String IPV6_ADDRESS = "2001:db8:85a3:0:0:8a2e:370:7334";
  private static final String IPV6_ADDRESS_ANON = "2001:db8:85a3:0:0:0:0:0";

  @Test(expected = InvalidIpAddressException.class)
  public void givenInvalidIpAddress_whenFromString_thenInvalidIpAddressException() {
    IpAddress.fromString("bad");
  }

  @Test
  public void givenIpv4Address_whenFromString_thenAnonymizedAndTypeIsIpv4() {
    IpAddress ipv4Address = IpAddress.fromString(IPV4_ADDRESS);
    assertEquals(IPV4_ADDRESS_ANON, ipv4Address.getIpAddress());
    assertEquals(IPV4, ipv4Address.getIpAddressType());
  }

  @Test
  public void givenIpv6Address_whenFromString_thenAnonymizedAndTypeIsIpv6() {
    IpAddress ipv6Address = IpAddress.fromString(IPV6_ADDRESS);
    assertEquals(IPV6_ADDRESS_ANON, ipv6Address.getIpAddress());
    assertEquals(IPV6, ipv6Address.getIpAddressType());
  }

}
