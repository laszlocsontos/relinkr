package com.springuni.hermes.core.security.authn;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.hermes.core.security.authn.jwt.JwtAuthenticationEntryPoint;
import com.springuni.hermes.core.security.authn.jwt.JwtAuthenticationFilter;
import com.springuni.hermes.core.security.authn.jwt.JwtTokenService;
import com.springuni.hermes.core.security.authn.oauth2.PersistentOAuth2UserService;
import com.springuni.hermes.core.security.authn.signin.DefaultAuthenticationFailureHandler;
import com.springuni.hermes.core.security.authn.signin.DefaultAuthenticationSuccessHandler;
import com.springuni.hermes.user.service.UserProfileFactory;
import com.springuni.hermes.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.NimbusAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final UserProfileFactory userProfileFactory;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new NimbusAuthorizationCodeTokenResponseClient();
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
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

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new DefaultAuthenticationSuccessHandler(jwtTokenService);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new DefaultAuthenticationFailureHandler(objectMapper);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint(objectMapper);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .oauth2Login()
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .authorizationEndpoint()
                .authorizationRequestRepository(authorizationRequestRepository())
                .and()
                .tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient())
                .and()
                .userInfoEndpoint().userService(oauth2UserService()).and()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .addFilterBefore(jwtAuthenticationFilter, LogoutFilter.class)
                .authorizeRequests()
                .regexMatchers(GET, "/").permitAll()
                .regexMatchers(GET, "/login/oauth2").permitAll()
                .regexMatchers(GET, "/[a-zA-Z0-9_-]{11}").permitAll()
                .regexMatchers("/api/.*").hasAuthority("ROLE_USER")
                .anyRequest().denyAll()
                .and();
    }

}
