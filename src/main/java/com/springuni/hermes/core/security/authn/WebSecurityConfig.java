package com.springuni.hermes.core.security.authn;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.hermes.core.security.authn.oauth2.PersistentOAuth2UserService;
import com.springuni.hermes.user.service.UserProfileFactory;
import com.springuni.hermes.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.NimbusAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final UserProfileFactory userProfileFactory;
    private final UserService userService;

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new NimbusAuthorizationCodeTokenResponseClient();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultOAuth2UserService() {
        return new DefaultOAuth2UserService();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return new PersistentOAuth2UserService(
                defaultOAuth2UserService(), userProfileFactory, userService
        );
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.debug(true);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .oauth2Login()
                .tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient()).and()
                .userInfoEndpoint().userService(oauth2UserService()).and()
                .loginPage("/login/oauth2")
                .defaultSuccessUrl("/pages/dashboard", true)
                .and()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(UNAUTHORIZED),
                        new RegexRequestMatcher("/api/.*", null)
                )
                .defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/login/oauth2"),
                        AnyRequestMatcher.INSTANCE
                )
                .and()
                .authorizeRequests()
                .regexMatchers(GET, "/").permitAll()
                .regexMatchers(GET, "/login/oauth2").permitAll()
                .regexMatchers(GET, "/[a-zA-Z0-9_-]{11}").permitAll()
                .regexMatchers(GET, "/vendor/.*").permitAll()
                .regexMatchers(GET, "/app/.*").permitAll()
                .regexMatchers(GET, "/pages/.*").hasAuthority("ROLE_USER")
                .regexMatchers("/api/.*").hasAuthority("ROLE_USER")
                .anyRequest().denyAll()
                .and();
    }

}
