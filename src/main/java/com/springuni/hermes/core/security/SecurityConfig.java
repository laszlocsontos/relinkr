package com.springuni.hermes.core.security;

import static com.springuni.hermes.core.security.signin.SignInFilter.SIGNIN_HTTP_METHOD;
import static com.springuni.hermes.core.security.signin.SignInFilter.SIGNIN_PROCESSES_URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springuni.hermes.core.security.jwt.JwtAuthenticationEntryPoint;
import com.springuni.hermes.core.security.jwt.JwtAuthenticationFilter;
import com.springuni.hermes.core.security.jwt.JwtAuthenticationProvider;
import com.springuni.hermes.core.security.jwt.JwtProperties;
import com.springuni.hermes.core.security.jwt.JwtTokenService;
import com.springuni.hermes.core.security.jwt.JwtTokenServiceImpl;
import com.springuni.hermes.core.security.signin.SignInFailureHandler;
import com.springuni.hermes.core.security.signin.SignInFilter;
import com.springuni.hermes.core.security.signin.SignInSuccessHandler;
import com.springuni.hermes.core.util.IdentityGenerator;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;

    @Bean
    public JwtTokenService jwtTokenService() {
        byte[] secretKey = Base64.getDecoder().decode(jwtProperties.getSecretKey());
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
        return new SignInSuccessHandler(jwtTokenService());
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SignInFailureHandler(objectMapper);
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
    public void configure(WebSecurity webSecurity) {
        webSecurity.debug(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(jwtAuthenticationProvider());
    }

    /*
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
                .regexMatchers("/api/.*").hasAuthority("USER")
                .anyRequest().denyAll()
                .and()
                .userDetailsService(userDetailsService)
                .addFilterAt(signInFilter(), UsernamePasswordAuthenticationFilter.class)
                // JwtAuthenticationFilter must precede LogoutFilter, otherwise LogoutHandler
                // wouldn't know who logs out.
                .addFilterBefore(jwtAuthenticationFilter(), LogoutFilter.class);
    }
    */

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .oauth2Login().defaultSuccessUrl("/pages/dashboard")
                .and()
                .authorizeRequests()
                // .regexMatchers("/").permitAll()
                .regexMatchers(HttpMethod.valueOf(SIGNIN_HTTP_METHOD), SIGNIN_PROCESSES_URL)
                .permitAll()
                .regexMatchers("/[a-zA-Z0-9_-]{11}").permitAll()
                .regexMatchers("/vendor/.*").permitAll()
                .regexMatchers("/dist/.*").permitAll()
                .regexMatchers("/api/.*").hasAuthority("ROLE_USER")
                .regexMatchers("/pages/.*").hasAuthority("ROLE_USER")
                .anyRequest().denyAll()
                .and();
    }

}
