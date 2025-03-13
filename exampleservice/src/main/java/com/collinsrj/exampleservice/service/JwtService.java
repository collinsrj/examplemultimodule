/*
                                * Copyright 2024 Collins
                                */
package com.collinsrj.exampleservice.service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration:86400}") // Default 24 hours
  private long expirationSeconds;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String username) {
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(username)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plus(Duration.ofSeconds(expirationSeconds))))
        .signWith(getSigningKey())
        .compact();
  }

  public String extractUsername(String token) {
    return extractClaims(token).getSubject();
  }

  private Claims extractClaims(String token) throws JwtException {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }

  public Authentication getAuthentication(String token) throws JwtException {
    Claims claims = extractClaims(token);
    if (claims.getExpiration().toInstant().isBefore(Instant.now())) {
      throw new JwtException("Token has expired");
    }
    return new UsernamePasswordAuthenticationToken(
        claims.getSubject(),
        null,
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
  }

  public boolean validateToken(String token) {
    try {
      Claims claims = extractClaims(token);
      return !claims.getExpiration().toInstant().isBefore(Instant.now());
    } catch (JwtException e) {
      return false;
    }
  }
}
