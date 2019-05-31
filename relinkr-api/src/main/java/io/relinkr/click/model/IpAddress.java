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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents an anonymized IP Address, which means that in case of an IPv4 address, the last octet,
 * and in case of an IPv6 address, the last 64 bits are reset to zero.
 */
@Getter
@Embeddable
public class IpAddress {

  // Preliminary regex match is to avoid hostname lookups when this class is constructed with
  // a non-numeric value.
  private static final Pattern IPV4_AND_6_PATTERN =
      Pattern.compile("([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4}|(\\d{1,3}\\.){3}\\d{1,3}");

  private static final int IPV6_ADDRESS_SIZE = 16;

  private String ipAddress;

  @Enumerated
  private IpAddressType ipAddressType;

  /*
   * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
   */
  IpAddress() {
  }

  private IpAddress(String ipAddress, IpAddressType ipAddressType) {
    this.ipAddress = ipAddress;
    this.ipAddressType = ipAddressType;
  }

  /**
   * Creates a new {@code IpAddress} instance from the given {@code ipAddress}.
   *
   * @param ipAddress String representation of an IPv4 ot an IPv6 address
   * @return a new {@code IpAddress} instance if {@code ipAddress} is a valid address
   * @throws InvalidIpAddressException is thrown if {@code ipAddress} is an invalid address
   */
  public static IpAddress fromString(@NonNull String ipAddress) throws InvalidIpAddressException {
    if (!IPV4_AND_6_PATTERN.matcher(ipAddress).matches()) {
      throw InvalidIpAddressException.forIpAddress(ipAddress);
    }

    InetAddress inetAddress = doGetByName(ipAddress);
    byte[] address = inetAddress.getAddress();

    // Anonymize IP address
    anonymizeAddress(address);

    inetAddress = doGetByAddress(address);
    return new IpAddress(inetAddress.getHostAddress(), IpAddressType.of(address));
  }

  private static void anonymizeAddress(byte[] address) {
    if (address.length == IPV6_ADDRESS_SIZE) {
      for (int index = address.length - 1; index > address.length - 8; index--) {
        address[index] = 0;
      }
      return;
    }

    address[address.length - 1] = 0;
  }

  private static InetAddress doGetByName(String host) {
    try {
      return InetAddress.getByName(host);
    } catch (UnknownHostException uhe) {
      throw new InvalidIpAddressException(uhe.getMessage(), uhe);
    }
  }

  private static InetAddress doGetByAddress(byte[] address) {
    try {
      return InetAddress.getByAddress(address);
    } catch (UnknownHostException uhe) {
      throw new InvalidIpAddressException(uhe.getMessage(), uhe);
    }
  }

  public enum IpAddressType {

    IPV4, IPV6;

    static IpAddressType of(byte[] address) {
      if (address.length == IPV6_ADDRESS_SIZE) {
        return IPV6;
      }

      return IPV4;
    }

  }

}
