/*
 * Copyright 2024 Collins
 */
package com.collinsrj.exampleservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collinsrj.exampleservice.model.Task;
import com.collinsrj.exampleservice.repository.TaskRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TaskService {
  private final TaskRepository taskRepository;

  @Autowired
  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public Flux<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  public Mono<Task> getTaskById(String id) {
    return taskRepository.findById(id);
  }

  public Flux<Task> getTasksByAuthor(String author) {
    return taskRepository.findByAuthor(author);
  }

  public Mono<Task> createTask(Task task) {
    return taskRepository.save(task);
  }

  public Mono<Task> updateTask(String id, Task task) {
    return taskRepository
        .findById(id)
        .flatMap(
            existingTask -> {
              task.setId(id);
              return taskRepository.save(task);
            });
  }

  public Mono<Void> deleteTask(String id) {
    return taskRepository.deleteById(id);
  }
}
