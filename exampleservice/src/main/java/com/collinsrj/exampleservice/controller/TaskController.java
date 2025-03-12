/*
 * Copyright 2024 Collins
 */
package com.collinsrj.exampleservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.collinsrj.exampleservice.dto.TaskDTO;
import com.collinsrj.exampleservice.model.Task;
import com.collinsrj.exampleservice.service.TaskService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;

  @Autowired
  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public Flux<Task> getAllTasks() {
    return taskService.getAllTasks();
  }

  @GetMapping("/{id}")
  public Mono<Task> getTaskById(@PathVariable String id) {
    return taskService.getTaskById(id);
  }

  @GetMapping("/author/{author}")
  public Flux<Task> getTasksByAuthor(@PathVariable String author) {
    return taskService.getTasksByAuthor(author);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Task> createTask(@Valid @RequestBody TaskDTO taskDTO) {
    return taskService.createTask(taskDTO.toEntity());
  }

  @PutMapping("/{id}")
  public Mono<Task> updateTask(@PathVariable String id, @Valid @RequestBody TaskDTO taskDTO) {
    return taskService.updateTask(id, taskDTO.toEntity());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteTask(@PathVariable String id) {
    return taskService.deleteTask(id);
  }
}
