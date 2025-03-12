/*
 * Copyright 2024 Collins
 */
package com.collinsrj.exampleservice.service;

import java.util.Collections;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secret;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String extractUsername(String token) {
    return extractClaims(token).getSubject();
  }

  private Claims extractClaims(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }

  public Authentication getAuthentication(String token) {
    Claims claims = extractClaims(token);
    return new UsernamePasswordAuthenticationToken(
        claims.getSubject(),
        null,
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
  }
}
