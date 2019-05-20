package com.springuni.hermes.core.security.authn.jwt;

import com.springuni.hermes.core.security.authn.jwt.JwtConfig.JwtProperties;
import com.springuni.hermes.core.util.IdentityGenerator;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.validation.annotation.Validated;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
    private static final KeyFactory RSA_KEY_FACTORY;

    static {
        try {
            RSA_KEY_FACTORY = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("Couldn't create RSA key factory", e);
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
    public JwtAuthenticationService jwtTokenService(JwtProperties jwtProperties) {
        return new JwtAuthenticationServiceImpl(
                jwtProperties.getPrivateKey(), jwtProperties.getPublicKey(),
                IdentityGenerator.getInstance()
        );
    }

    @Bean
    public JwtAuthenticationTokenCookieResolver jwtAuthenticationTokenCookieResolver() {
        return new JwtAuthenticationTokenCookieResolverImpl();
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
            } catch (InvalidKeySpecException e) {
                throw new IllegalArgumentException("Couldn't generate key", e);
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
