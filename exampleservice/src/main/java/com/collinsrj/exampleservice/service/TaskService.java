/*
                                * Copyright 2024 Collins
                                */
package com.collinsrj.exampleservice.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
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

  private Mono<String> getCurrentUsername() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(authentication -> authentication.getName());
  }

  private Mono<Boolean> isAdmin() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(
            authentication ->
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
  }

  public Flux<Task> getAllTasks() {
    return isAdmin()
        .flatMapMany(
            isAdmin -> {
              if (isAdmin) {
                return taskRepository.findAll();
              }
              return getCurrentUsername()
                  .flatMapMany(username -> taskRepository.findByAuthor(username));
            });
  }

  public Mono<Task> getTaskById(String id) {
    return isAdmin()
        .flatMap(
            isAdmin -> {
              if (isAdmin) {
                return taskRepository.findById(id);
              }
              return getCurrentUsername()
                  .flatMap(username -> taskRepository.findByIdAndAuthor(id, username));
            });
  }

  public Flux<Task> getTasksByAuthor(String author) {
    return isAdmin()
        .flatMapMany(
            isAdmin -> {
              if (isAdmin) {
                return taskRepository.findByAuthor(author);
              }
              return getCurrentUsername()
                  .flatMapMany(
                      username -> {
                        if (username.equals(author)) {
                          return taskRepository.findByAuthor(author);
                        }
                        return Flux.empty();
                      });
            });
  }

  public Mono<Task> createTask(Task task) {
    return getCurrentUsername()
        .flatMap(
            username -> {
              Task defensiveCopy = new Task();
              defensiveCopy.setId(task.getId());
              defensiveCopy.setTitle(task.getTitle());
              defensiveCopy.setDescription(task.getDescription());
              defensiveCopy.setDueDate(task.getDueDate());
              defensiveCopy.setAuthor(username);
              defensiveCopy.setStatus(task.getStatus());
              defensiveCopy.setCreatedAt(task.getCreatedAt());
              defensiveCopy.setUpdatedAt(task.getUpdatedAt());
              return taskRepository.save(defensiveCopy);
            });
  }

  public Mono<Task> updateTask(String id, Task task) {
    return isAdmin()
        .flatMap(
            isAdmin -> {
              if (isAdmin) {
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
              return getCurrentUsername()
                  .flatMap(
                      username ->
                          taskRepository
                              .findByIdAndAuthor(id, username)
                              .flatMap(
                                  existingTask -> {
                                    Task updatedTask = new Task();
                                    updatedTask.setId(id);
                                    updatedTask.setTitle(task.getTitle());
                                    updatedTask.setDescription(task.getDescription());
                                    updatedTask.setDueDate(task.getDueDate());
                                    updatedTask.setAuthor(username);
                                    updatedTask.setStatus(task.getStatus());
                                    updatedTask.setCreatedAt(existingTask.getCreatedAt());
                                    updatedTask.setUpdatedAt(task.getUpdatedAt());
                                    return taskRepository.save(updatedTask);
                                  }));
            });
  }

  public Mono<Void> deleteTask(String id) {
    return isAdmin()
        .flatMap(
            isAdmin -> {
              if (isAdmin) {
                return taskRepository.deleteById(id);
              }
              return getCurrentUsername()
                  .flatMap(username -> taskRepository.deleteByIdAndAuthor(id, username));
            });
  }
}
