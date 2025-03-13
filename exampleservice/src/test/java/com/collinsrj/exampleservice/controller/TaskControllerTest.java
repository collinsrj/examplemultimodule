/*
                                * Copyright 2024 Collins
                                */
package com.collinsrj.exampleservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.collinsrj.exampleservice.dto.TaskDTO;
import com.collinsrj.exampleservice.dto.TaskResponseDTO;
import com.collinsrj.exampleservice.model.Task;
import com.collinsrj.exampleservice.service.TaskService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class TaskControllerTest {

  @Mock private TaskService taskService;

  private TaskController taskController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    taskController = new TaskController(taskService);
  }

  @Test
  void getAllTasks() {
    Task task1 = createTask("1", "Task 1");
    Task task2 = createTask("2", "Task 2");
    when(taskService.getAllTasks()).thenReturn(Flux.just(task1, task2));

    StepVerifier.create(taskController.getAllTasks())
        .expectNext(TaskResponseDTO.fromEntity(task1))
        .expectNext(TaskResponseDTO.fromEntity(task2))
        .verifyComplete();

    verify(taskService).getAllTasks();
  }

  @Test
  void getTaskById() {
    Task task = createTask("1", "Task 1");
    when(taskService.getTaskById("1")).thenReturn(Mono.just(task));

    StepVerifier.create(taskController.getTaskById("1"))
        .expectNext(TaskResponseDTO.fromEntity(task))
        .verifyComplete();

    verify(taskService).getTaskById("1");
  }

  @Test
  void getTasksByAuthor() {
    Task task1 = createTask("1", "Task 1");
    Task task2 = createTask("2", "Task 2");
    when(taskService.getTasksByAuthor("author")).thenReturn(Flux.just(task1, task2));

    StepVerifier.create(taskController.getTasksByAuthor("author"))
        .expectNext(TaskResponseDTO.fromEntity(task1))
        .expectNext(TaskResponseDTO.fromEntity(task2))
        .verifyComplete();

    verify(taskService).getTasksByAuthor("author");
  }

  @Test
  void createTask() {
    Task task = createTask("1", "Task 1");
    TaskDTO taskDTO = createTaskDTO("Task 1");
    when(taskService.createTask(any(Task.class))).thenReturn(Mono.just(task));

    StepVerifier.create(taskController.createTask(taskDTO))
        .expectNext(TaskResponseDTO.fromEntity(task))
        .verifyComplete();

    verify(taskService).createTask(any(Task.class));
  }

  @Test
  void updateTask() {
    Task task = createTask("1", "Updated Task");
    TaskDTO taskDTO = createTaskDTO("Updated Task");
    when(taskService.updateTask(eq("1"), any(Task.class))).thenReturn(Mono.just(task));

    StepVerifier.create(taskController.updateTask("1", taskDTO))
        .expectNext(TaskResponseDTO.fromEntity(task))
        .verifyComplete();

    verify(taskService).updateTask(eq("1"), any(Task.class));
  }

  @Test
  void deleteTask() {
    when(taskService.deleteTask("1")).thenReturn(Mono.empty());

    StepVerifier.create(taskController.deleteTask("1")).verifyComplete();

    verify(taskService).deleteTask("1");
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

  private TaskDTO createTaskDTO(String title) {
    TaskDTO taskDTO = new TaskDTO();
    taskDTO.setTitle(title);
    taskDTO.setDescription("Test Description");
    taskDTO.setDueDate(LocalDateTime.now());
    taskDTO.setAuthor("test.author");
    taskDTO.setStatus(Task.TaskStatus.TODO);
    return taskDTO;
  }
}
