/*
 * Copyright 2024 Collins
 */
package com.collinsrj.exampleservice.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.collinsrj.exampleservice.model.Task;
import com.collinsrj.exampleservice.repository.TaskRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class TaskServiceTest {

  @Mock private TaskRepository taskRepository;

  private TaskService taskService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    taskService = new TaskService(taskRepository);
  }

  @Test
  void getAllTasks() {
    Task task1 = createTask("1", "Task 1");
    Task task2 = createTask("2", "Task 2");
    when(taskRepository.findAll()).thenReturn(Flux.just(task1, task2));

    StepVerifier.create(taskService.getAllTasks())
        .expectNext(task1)
        .expectNext(task2)
        .verifyComplete();

    verify(taskRepository).findAll();
  }

  @Test
  void getTaskById() {
    Task task = createTask("1", "Task 1");
    when(taskRepository.findById("1")).thenReturn(Mono.just(task));

    StepVerifier.create(taskService.getTaskById("1")).expectNext(task).verifyComplete();

    verify(taskRepository).findById("1");
  }

  @Test
  void getTasksByAuthor() {
    Task task1 = createTask("1", "Task 1");
    Task task2 = createTask("2", "Task 2");
    String author = "test.author";
    when(taskRepository.findByAuthor(author)).thenReturn(Flux.just(task1, task2));

    StepVerifier.create(taskService.getTasksByAuthor(author))
        .expectNext(task1)
        .expectNext(task2)
        .verifyComplete();

    verify(taskRepository).findByAuthor(author);
  }

  @Test
  void createTask() {
    Task task = createTask("1", "Task 1");
    when(taskRepository.save(any(Task.class))).thenReturn(Mono.just(task));

    StepVerifier.create(taskService.createTask(task)).expectNext(task).verifyComplete();

    verify(taskRepository).save(task);
  }

  @Test
  void updateTask() {
    Task existingTask = createTask("1", "Task 1");
    Task updatedTask = createTask("1", "Updated Task 1");

    when(taskRepository.findById("1")).thenReturn(Mono.just(existingTask));
    when(taskRepository.save(any(Task.class))).thenReturn(Mono.just(updatedTask));

    StepVerifier.create(taskService.updateTask("1", updatedTask))
        .expectNext(updatedTask)
        .verifyComplete();

    verify(taskRepository).findById("1");
    verify(taskRepository).save(updatedTask);
  }

  @Test
  void deleteTask() {
    when(taskRepository.deleteById("1")).thenReturn(Mono.empty());

    StepVerifier.create(taskService.deleteTask("1")).verifyComplete();

    verify(taskRepository).deleteById("1");
  }

  private Task createTask(String id, String title) {
    Task task = new Task();
    task.setId(id);
    task.setTitle(title);
    task.setDescription("Test Description");
    task.setDueDate(LocalDateTime.now());
    task.setAuthor("test.author");
    task.setStatus(Task.TaskStatus.TODO);
    return task;
  }
}
