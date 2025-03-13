/*
                                * Copyright 2024 Collins
                                */
package com.collinsrj.exampleservice.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.collinsrj.exampleservice.model.Task;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class TaskDTOTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testValidTaskDTO() {
    TaskDTO taskDTO = new TaskDTO();
    taskDTO.setTitle("Test Task");
    taskDTO.setDescription("Test Description");
    taskDTO.setDueDate(LocalDateTime.now().plusDays(1));
    taskDTO.setStatus(Task.TaskStatus.TODO);

    Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);
    assertTrue(violations.isEmpty(), "TaskDTO should be valid");
  }

  @Test
  void testInvalidTaskDTO_MissingTitle() {
    TaskDTO taskDTO = new TaskDTO();
    taskDTO.setDescription("Test Description");
    taskDTO.setDueDate(LocalDateTime.now().plusDays(1));
    taskDTO.setStatus(Task.TaskStatus.TODO);

    Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);
    assertEquals(1, violations.size(), "Should have one validation error");
    assertEquals("Title is required", violations.iterator().next().getMessage());
  }

  @Test
  void testInvalidTaskDTO_MissingDescription() {
    TaskDTO taskDTO = new TaskDTO();
    taskDTO.setTitle("Test Task");
    taskDTO.setDueDate(LocalDateTime.now().plusDays(1));
    taskDTO.setStatus(Task.TaskStatus.TODO);

    Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);
    assertEquals(1, violations.size(), "Should have one validation error");
    assertEquals("Description is required", violations.iterator().next().getMessage());
  }

  @Test
  void testInvalidTaskDTO_MissingStatus() {
    TaskDTO taskDTO = new TaskDTO();
    taskDTO.setTitle("Test Task");
    taskDTO.setDescription("Test Description");
    taskDTO.setDueDate(LocalDateTime.now().plusDays(1));

    Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);
    assertEquals(1, violations.size(), "Should have one validation error");
    assertEquals("Status is required", violations.iterator().next().getMessage());
  }

  @Test
  void testToEntity() {
    TaskDTO taskDTO = new TaskDTO();
    taskDTO.setTitle("Test Task");
    taskDTO.setDescription("Test Description");
    taskDTO.setDueDate(LocalDateTime.now().plusDays(1));
    taskDTO.setStatus(Task.TaskStatus.TODO);

    Task task = taskDTO.toEntity();

    assertNull(task.getId(), "ID should be null for new task");
    assertEquals(taskDTO.getTitle(), task.getTitle(), "Title should match");
    assertEquals(taskDTO.getDescription(), task.getDescription(), "Description should match");
    assertEquals(taskDTO.getDueDate(), task.getDueDate(), "Due date should match");
    assertEquals(taskDTO.getStatus(), task.getStatus(), "Status should match");
    assertNull(task.getAuthor(), "Author should be null as it's set by service layer");
    assertNull(task.getCreatedAt(), "CreatedAt should be null for new task");
    assertNull(task.getUpdatedAt(), "UpdatedAt should be null for new task");
  }
}
