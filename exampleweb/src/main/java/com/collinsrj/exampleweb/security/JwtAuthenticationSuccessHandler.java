/*
                                * Copyright 2024 Collins
                                */
package com.collinsrj.exampleweb.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;

import com.collinsrj.exampleweb.service.JwtService;

import reactor.core.publisher.Mono;

public class JwtAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
  private final JwtService jwtService;
  private final ServerAuthenticationSuccessHandler delegate;

  public JwtAuthenticationSuccessHandler(
      JwtService jwtService, ServerAuthenticationSuccessHandler delegate) {
    this.jwtService = jwtService;
    this.delegate = delegate;
  }

  @Override
  public Mono<Void> onAuthenticationSuccess(
      WebFilterExchange webFilterExchange, Authentication authentication) {
    return Mono.just(authentication)
        .map(
            auth -> {
              UserDetails userDetails = (UserDetails) auth.getPrincipal();
              String token = jwtService.generateToken(userDetails);
              return token;
            })
        .flatMap(
            token ->
                webFilterExchange
                    .getExchange()
                    .getSession()
                    .doOnNext(session -> session.getAttributes().put("jwt_token", token)))
        .then(delegate.onAuthenticationSuccess(webFilterExchange, authentication));
  }
}
