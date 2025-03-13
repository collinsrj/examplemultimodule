/*
                                * Copyright 2024 Collins
                                */
package com.collinsrj.exampleservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.collinsrj.exampleservice.dto.TaskDTO;
import com.collinsrj.exampleservice.dto.TaskResponseDTO;
import com.collinsrj.exampleservice.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tasks")
@Validated
@RequiredArgsConstructor
@Slf4j
public class TaskController {
  private final TaskService taskService;

  @GetMapping
  public Flux<TaskResponseDTO> getAllTasks() {
    return taskService.getAllTasks().map(TaskResponseDTO::fromEntity);
  }

  @GetMapping("/{id}")
  public Mono<TaskResponseDTO> getTaskById(@PathVariable String id) {
    return taskService.getTaskById(id).map(TaskResponseDTO::fromEntity);
  }

  @GetMapping("/author/{author}")
  public Flux<TaskResponseDTO> getTasksByAuthor(@PathVariable String author) {
    return taskService.getTasksByAuthor(author).map(TaskResponseDTO::fromEntity);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<TaskResponseDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
    return taskService.createTask(taskDTO.toEntity()).map(TaskResponseDTO::fromEntity);
  }

  @PutMapping("/{id}")
  public Mono<TaskResponseDTO> updateTask(
      @PathVariable String id, @Valid @RequestBody TaskDTO taskDTO) {
    return taskService.updateTask(id, taskDTO.toEntity()).map(TaskResponseDTO::fromEntity);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteTask(@PathVariable String id) {
    return taskService.deleteTask(id);
  }
}
