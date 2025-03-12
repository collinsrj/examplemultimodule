/*
 * Copyright 2024 Collins
 */
package com.collinsrj.exampleweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

import com.collinsrj.exampleweb.security.JwtAuthenticationSuccessHandler;
import com.collinsrj.exampleweb.service.JwtService;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  private final JwtService jwtService;

  public SecurityConfig(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(
            exchanges ->
                exchanges
                    .pathMatchers("/login", "/css/**", "/js/**", "/images/**", "/webjars/**")
                    .permitAll()
                    .anyExchange()
                    .authenticated())
        .formLogin(
            formLogin ->
                formLogin
                    .loginPage("/login")
                    .authenticationSuccessHandler(authenticationSuccessHandler()))
        .securityContextRepository(securityContextRepository())
        .logout(logout -> logout.logoutUrl("/logout"))
        .build();
  }

  @Bean
  public ServerSecurityContextRepository securityContextRepository() {
    return new WebSessionServerSecurityContextRepository();
  }

  @Bean
  public ServerAuthenticationSuccessHandler authenticationSuccessHandler() {
    return new JwtAuthenticationSuccessHandler(
        jwtService, new RedirectServerAuthenticationSuccessHandler("/tasks"));
  }

  @Bean
  public ReactiveAuthenticationManager authenticationManager(
      MapReactiveUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    var authenticationManager =
        new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    authenticationManager.setPasswordEncoder(passwordEncoder);
    return authenticationManager;
  }

  @Bean
  public MapReactiveUserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails user =
        User.builder()
            .username("user")
            .password(passwordEncoder.encode("password"))
            .roles("USER")
            .build();
    return new MapReactiveUserDetailsService(user);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
