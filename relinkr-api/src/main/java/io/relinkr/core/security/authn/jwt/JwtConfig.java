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

package io.relinkr.core.security.authn.jwt;

import io.relinkr.core.security.authn.jwt.JwtConfig.JwtProperties;
import io.relinkr.core.util.IdGenerator;
import io.relinkr.core.util.IdentityGenerator;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Clock;
import java.util.Base64;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;

/**
 * JWT related bean wiring and reading pertinent configuration options from application's
 * properties.
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

  private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
  private static final KeyFactory RSA_KEY_FACTORY;

  static {
    try {
      RSA_KEY_FACTORY = KeyFactory.getInstance("RSA");
    } catch (NoSuchAlgorithmException nsae) {
      throw new AssertionError("Couldn't create RSA key factory", nsae);
    }
  }

  @Bean
  @ConfigurationPropertiesBinding
  public Converter<String, PrivateKey> base64EncodedDerToPrivateKeyConverter() {
    return new Base64EncodedDerToPrivateKeyConverter();
  }

  @Bean
  @ConfigurationPropertiesBinding
  public Converter<String, PublicKey> base64EncodedDerToPublicKeyConverter() {
    return new Base64EncodedDerToPublicKeyConverter();
  }

  @Bean
  public JwtAuthenticationService jwtTokenService(
      ObjectProvider<Clock> clock, IdGenerator idGenerator, JwtProperties jwtProperties) {
    return new JwtAuthenticationServiceImpl(
        clock,
        jwtProperties.getPrivateKey(), jwtProperties.getPublicKey(),
        idGenerator
    );
  }

  @Bean
  public JwtAuthenticationTokenCookieResolver jwtAuthenticationTokenCookieResolver(
      Environment environment) {

    return new JwtAuthenticationTokenCookieResolverImpl(environment);
  }

  @Data
  @Validated
  @ConfigurationProperties("relinkr.jwt")
  static class JwtProperties {

    @NotNull
    private PrivateKey privateKey;

    @NotNull
    private PublicKey publicKey;

  }

  @RequiredArgsConstructor
  private abstract static class Base64EncodedDerToKeyConverter<K extends Key>
      implements Converter<String, K> {

    @Override
    public K convert(String source) {
      byte[] encodedKey = BASE64_DECODER.decode(source);
      KeySpec keySpec = decodeKey(encodedKey);
      try {
        return generateKey(keySpec);
      } catch (InvalidKeySpecException ikse) {
        throw new IllegalArgumentException("Couldn't generate key", ikse);
      }
    }

    abstract KeySpec decodeKey(byte[] encodedKey);

    abstract K generateKey(KeySpec keySpec) throws InvalidKeySpecException;

  }

  private static class Base64EncodedDerToPrivateKeyConverter
      extends Base64EncodedDerToKeyConverter<PrivateKey> {

    @Override
    KeySpec decodeKey(byte[] encodedKey) {
      return new PKCS8EncodedKeySpec(encodedKey);
    }

    @Override
    PrivateKey generateKey(KeySpec keySpec) throws InvalidKeySpecException {
      return RSA_KEY_FACTORY.generatePrivate(keySpec);
    }

  }

  private static class Base64EncodedDerToPublicKeyConverter
      extends Base64EncodedDerToKeyConverter<PublicKey> {

    @Override
    KeySpec decodeKey(byte[] encodedKey) {
      return new X509EncodedKeySpec(encodedKey);
    }

    @Override
    PublicKey generateKey(KeySpec keySpec) throws InvalidKeySpecException {
      return RSA_KEY_FACTORY.generatePublic(keySpec);
    }

  }

}
