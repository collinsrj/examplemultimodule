/*
                                * Copyright 2024 Collins
                                */
package com.collinsrj.exampleservice.service;

import org.springframework.stereotype.Service;

import com.collinsrj.exampleservice.model.Task;
import com.collinsrj.exampleservice.repository.TaskRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public final class TaskService {
  private final TaskRepository taskRepository;

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
    Task defensiveCopy = new Task();
    defensiveCopy.setId(task.getId());
    defensiveCopy.setTitle(task.getTitle());
    defensiveCopy.setDescription(task.getDescription());
    defensiveCopy.setDueDate(task.getDueDate());
    defensiveCopy.setAuthor(task.getAuthor());
    defensiveCopy.setStatus(task.getStatus());
    defensiveCopy.setCreatedAt(task.getCreatedAt());
    defensiveCopy.setUpdatedAt(task.getUpdatedAt());
    return taskRepository.save(defensiveCopy);
  }

  public Mono<Task> updateTask(String id, Task task) {
    return taskRepository
        .findById(id)
        .flatMap(
            existingTask -> {
              Task updatedTask = new Task();
              updatedTask.setId(id);
              updatedTask.setTitle(task.getTitle());
              updatedTask.setDescription(task.getDescription());
              updatedTask.setDueDate(task.getDueDate());
              updatedTask.setAuthor(task.getAuthor());
              updatedTask.setStatus(task.getStatus());
              updatedTask.setCreatedAt(existingTask.getCreatedAt());
              updatedTask.setUpdatedAt(task.getUpdatedAt());
              return taskRepository.save(updatedTask);
            });
  }

  public Mono<Void> deleteTask(String id) {
    return taskRepository.deleteById(id);
  }
}
