/*
                                * Copyright 2024 Collins
                                */
package com.collinsrj.exampleservice.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.collinsrj.exampleservice.model.Task;

class TaskDTOTest {

  @Test
  void testTaskDTOProperties() {
    TaskDTO taskDTO = new TaskDTO();
    String title = "Test Task";
    String description = "Test Description";
    LocalDateTime dueDate = LocalDateTime.now();
    String author = "test.author";
    Task.TaskStatus status = Task.TaskStatus.TODO;

    taskDTO.setTitle(title);
    taskDTO.setDescription(description);
    taskDTO.setDueDate(dueDate);
    taskDTO.setAuthor(author);
    taskDTO.setStatus(status);

    assertEquals(title, taskDTO.getTitle());
    assertEquals(description, taskDTO.getDescription());
    assertEquals(dueDate, taskDTO.getDueDate());
    assertEquals(author, taskDTO.getAuthor());
    assertEquals(status, taskDTO.getStatus());
  }

  @Test
  void testToEntity() {
    TaskDTO taskDTO = new TaskDTO();
    String title = "Test Task";
    String description = "Test Description";
    LocalDateTime dueDate = LocalDateTime.now();
    String author = "test.author";
    Task.TaskStatus status = Task.TaskStatus.TODO;

    taskDTO.setTitle(title);
    taskDTO.setDescription(description);
    taskDTO.setDueDate(dueDate);
    taskDTO.setAuthor(author);
    taskDTO.setStatus(status);

    Task task = taskDTO.toEntity();

    assertNull(task.getId()); // ID should be null for new tasks
    assertEquals(title, task.getTitle());
    assertEquals(description, task.getDescription());
    assertEquals(dueDate, task.getDueDate());
    assertEquals(author, task.getAuthor());
    assertEquals(status, task.getStatus());
  }
}
