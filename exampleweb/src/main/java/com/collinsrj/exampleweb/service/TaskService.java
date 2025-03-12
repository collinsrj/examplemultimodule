/*
 * Copyright 2024 Collins
 */
package com.collinsrj.exampleweb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.collinsrj.exampleweb.model.Task;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TaskService {
  private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
  private final WebClient webClient;
  private final JwtService jwtService;

  public TaskService(@Value("${backend.service.url}") String backendUrl, JwtService jwtService) {
    this.jwtService = jwtService;
    this.webClient =
        WebClient.builder()
            .baseUrl(backendUrl)
            .filter(
                ExchangeFilterFunction.ofRequestProcessor(
                    request ->
                        ReactiveSecurityContextHolder.getContext()
                            .map(context -> context.getAuthentication())
                            .map(
                                auth -> {
                                  String token = generateToken(auth);
                                  logger.debug("Adding JWT token to request: {}", token);
                                  return ClientRequest.from(request)
                                      .headers(headers -> headers.setBearerAuth(token))
                                      .build();
                                })
                            .defaultIfEmpty(request)))
            .filter(logRequest())
            .filter(logResponse())
            .build();
  }

  private String generateToken(Authentication auth) {
    return jwtService.generateToken(auth.getName(), auth.getAuthorities());
  }

  private ExchangeFilterFunction logRequest() {
    return ExchangeFilterFunction.ofRequestProcessor(
        clientRequest -> {
          logger.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
          return Mono.just(clientRequest);
        });
  }

  private ExchangeFilterFunction logResponse() {
    return ExchangeFilterFunction.ofResponseProcessor(
        clientResponse -> {
          logger.debug("Response status: {}", clientResponse.statusCode());
          return Mono.just(clientResponse);
        });
  }

  public Flux<Task> getAllTasks() {
    return webClient
        .get()
        .uri("/tasks")
        .retrieve()
        .bodyToFlux(Task.class)
        .doOnError(
            error -> {
              if (error instanceof WebClientResponseException) {
                WebClientResponseException wcError = (WebClientResponseException) error;
                logger.error(
                    "Error getting tasks. Status: {}, Body: {}",
                    wcError.getStatusCode(),
                    wcError.getResponseBodyAsString());
              } else {
                logger.error("Error getting tasks", error);
              }
            });
  }

  public Mono<Task> getTaskById(String id) {
    return webClient
        .get()
        .uri("/tasks/{id}", id)
        .retrieve()
        .bodyToMono(Task.class)
        .doOnError(error -> logger.error("Error getting task by id: {}", id, error));
  }

  public Flux<Task> getTasksByAuthor(String author) {
    return webClient
        .get()
        .uri("/tasks/author/{author}", author)
        .retrieve()
        .bodyToFlux(Task.class)
        .doOnError(error -> logger.error("Error getting tasks by author: {}", author, error));
  }

  public Mono<Task> createTask(Task task) {
    return webClient
        .post()
        .uri("/tasks")
        .bodyValue(task)
        .retrieve()
        .bodyToMono(Task.class)
        .doOnError(error -> logger.error("Error creating task: {}", task, error));
  }

  public Mono<Task> updateTask(String id, Task task) {
    return webClient
        .put()
        .uri("/tasks/{id}", id)
        .bodyValue(task)
        .retrieve()
        .bodyToMono(Task.class)
        .doOnError(error -> logger.error("Error updating task: {}, {}", id, task, error));
  }

  public Mono<Void> deleteTask(String id) {
    return webClient
        .delete()
        .uri("/tasks/{id}", id)
        .retrieve()
        .bodyToMono(Void.class)
        .doOnError(error -> logger.error("Error deleting task: {}", id, error));
  }
}
