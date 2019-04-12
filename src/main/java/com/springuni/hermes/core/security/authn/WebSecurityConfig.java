package com.springuni.hermes.core.security.authn;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.hermes.core.security.authn.jwt.JwtAuthenticationEntryPoint;
import com.springuni.hermes.core.security.authn.jwt.JwtAuthenticationFilter;
import com.springuni.hermes.core.security.authn.jwt.JwtAuthenticationService;
import com.springuni.hermes.core.security.authn.oauth2.PersistentOAuth2UserService;
import com.springuni.hermes.core.security.authn.handler.DefaultAuthenticationFailureHandler;
import com.springuni.hermes.core.security.authn.handler.DefaultAuthenticationSuccessHandler;
import com.springuni.hermes.user.service.UserProfileFactory;
import com.springuni.hermes.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String OAUTH2_REDIRECT_REQUEST_BASE_URI = "/oauth2/redirect";
    public static final String OAUTH2_REDIRECT_REQUEST_URI =
            OAUTH2_REDIRECT_REQUEST_BASE_URI + "/*";

    public static final String OAUTH2_LOGIN_PROCESSES_BASE_URI = "/oauth2/login";
    public static final String OAUTH2_LOGIN_PROCESSES_URI =
            OAUTH2_LOGIN_PROCESSES_BASE_URI + "/*";

    public static final List<RequestMatcher> PUBLIC_REQUEST_MATCHERS = unmodifiableList(
            asList(
                    new AntPathRequestMatcher("/", "GET"),
                    new AntPathRequestMatcher(OAUTH2_LOGIN_PROCESSES_URI, "POST"),
                    new AntPathRequestMatcher(OAUTH2_REDIRECT_REQUEST_URI, "GET"),
                    new RegexRequestMatcher("/[a-zA-Z0-9_-]{11}", "GET")
            )
    );

    public static final List<RequestMatcher> PROTECTED_REQUEST_MATCHERS = unmodifiableList(
            singletonList(new RegexRequestMatcher("/api/.*", null))
    );

    private final ObjectMapper objectMapper;
    private final UserProfileFactory userProfileFactory;
    private final UserService userService;
    private final JwtAuthenticationService jwtAuthenticationService;

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
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new DefaultAuthenticationSuccessHandler(objectMapper, jwtAuthenticationService);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new DefaultAuthenticationFailureHandler(objectMapper);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
                authenticationManagerBean(),
                authenticationFailureHandler()
        );

        jwtAuthenticationFilter.setIgnoredRequestMatchers(PUBLIC_REQUEST_MATCHERS);

        return jwtAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationService);
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
                .redirectionEndpoint()
                .baseUri(OAUTH2_LOGIN_PROCESSES_URI)
                .and()
                .authorizationEndpoint()
                .baseUri(OAUTH2_REDIRECT_REQUEST_BASE_URI)
                .authorizationRequestRepository(authorizationRequestRepository())
                .and()
                .tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient())
                .and()
                .userInfoEndpoint().userService(oauth2UserService()).and()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), LogoutFilter.class)
                .authorizeRequests()
                .requestMatchers(asArray(PUBLIC_REQUEST_MATCHERS)).permitAll()
                .requestMatchers(asArray(PROTECTED_REQUEST_MATCHERS)).hasAuthority("ROLE_USER")
                .anyRequest().denyAll()
                .and();
    }

    private RequestMatcher[] asArray(List<RequestMatcher> requestMatchers) {
        return requestMatchers.toArray(new RequestMatcher[0]);
    }

}
