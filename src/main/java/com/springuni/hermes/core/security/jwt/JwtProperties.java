package com.springuni.hermes.core.security.jwt;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("jwt")
public class JwtProperties {

    @NotBlank
    private String secretKey;

}
