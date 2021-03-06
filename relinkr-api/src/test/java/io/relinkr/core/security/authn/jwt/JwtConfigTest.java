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

import static io.relinkr.test.Mocks.FIXED_CLOCK;
import static org.junit.Assert.assertEquals;

import io.relinkr.core.security.authn.jwt.JwtConfig.JwtProperties;
import io.relinkr.core.security.authn.jwt.JwtConfigTest.TestConfig;
import io.relinkr.core.util.IdGenerator;
import io.relinkr.core.util.IdentityGenerator;
import io.relinkr.core.util.RandomGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Clock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
public class JwtConfigTest {

  @Autowired
  private JwtProperties jwtProperties;

  @Test
  public void shouldLoadPrivateKey() {
    PrivateKey privateKey = jwtProperties.getPrivateKey();
    assertEquals("RSA", privateKey.getAlgorithm());
    assertEquals("PKCS#8", privateKey.getFormat());
  }

  @Test
  public void shouldLoadPublicKey() {
    PublicKey publicKey = jwtProperties.getPublicKey();
    assertEquals("RSA", publicKey.getAlgorithm());
    assertEquals("X.509", publicKey.getFormat());
  }

  @Configuration
  @Import(JwtConfig.class)
  static class TestConfig {

    @Bean
    Clock clock() {
      return FIXED_CLOCK;
    }

    @Bean
    RandomGenerator randomGenerator() {
      return new RandomGenerator();
    }

    @Bean
    IdGenerator idGenerator(RandomGenerator randomGenerator) {
      return new IdentityGenerator(randomGenerator);
    }

  }

}
