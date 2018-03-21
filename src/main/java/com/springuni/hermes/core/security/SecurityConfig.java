package com.springuni.hermes.core.security;

import static com.springuni.hermes.core.security.user.SignInFilter.SIGNIN_HTTP_METHOD;
import static com.springuni.hermes.core.security.user.SignInFilter.SIGNIN_PROCESSES_URL;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.hermes.core.security.jwt.JwtAuthenticationEntryPoint;
import com.springuni.hermes.core.security.jwt.JwtAuthenticationFilter;
import com.springuni.hermes.core.security.jwt.JwtAuthenticationProvider;
import com.springuni.hermes.core.security.jwt.JwtTokenService;
import com.springuni.hermes.core.security.jwt.JwtTokenServiceImpl;
import com.springuni.hermes.core.security.user.DelegatingUserDetailsService;
import com.springuni.hermes.core.security.user.SignInFilter;
import com.springuni.hermes.core.security.user.UsernamePasswordAuthenticationProvider;
import com.springuni.hermes.core.util.IdentityGenerator;
import com.springuni.hermes.user.service.UserService;
import java.util.Base64;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@ConfigurationProperties("jwt")
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final UserService userService;

    @NotBlank
    private String secretKey;

    @Bean
    public JwtTokenService jwtTokenService() {
        byte[] secretKey = Base64.getDecoder().decode(this.secretKey);
        return new JwtTokenServiceImpl(secretKey, IdentityGenerator.getInstance());
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenService());
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider();
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new DefaultAuthenticationSuccessHandler(jwtTokenService());
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new DefaultAuthenticationFailureHandler(objectMapper);
    }

    @Bean
    public AbstractAuthenticationProcessingFilter signInFilter() throws Exception {
        AbstractAuthenticationProcessingFilter signInFilter = new SignInFilter(objectMapper);

        signInFilter.setAuthenticationManager(authenticationManagerBean());
        signInFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        signInFilter.setAuthenticationFailureHandler(authenticationFailureHandler());

        return signInFilter;
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return new DelegatingUserDetailsService(userService);
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.debug(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(jwtAuthenticationProvider())
                .authenticationProvider(new UsernamePasswordAuthenticationProvider(userService));
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .regexMatchers("/").permitAll()
                .regexMatchers(HttpMethod.valueOf(SIGNIN_HTTP_METHOD), SIGNIN_PROCESSES_URL)
                .permitAll()
                .regexMatchers("/[a-zA-Z0-9_-]{11}").permitAll()
                .regexMatchers("/api/.*").hasAuthority("ROLE_USER")
                .anyRequest().denyAll()
                .and()
                .addFilterAt(signInFilter(), UsernamePasswordAuthenticationFilter.class)
                // JwtAuthenticationFilter must precede LogoutFilter, otherwise LogoutHandler
                // wouldn't know who logs out.
                .addFilterBefore(jwtAuthenticationFilter(), LogoutFilter.class);
    }

}
