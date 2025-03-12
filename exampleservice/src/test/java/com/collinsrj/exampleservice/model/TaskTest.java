/*
 * Copyright 2024 Collins
 */
package com.collinsrj.exampleservice.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class TaskTest {

  @Test
  void testTaskProperties() {
    Task task = new Task();
    String id = "123";
    String title = "Test Task";
    String description = "Test Description";
    LocalDateTime dueDate = LocalDateTime.now();
    String author = "test.author";
    Task.TaskStatus status = Task.TaskStatus.TODO;

    task.setId(id);
    task.setTitle(title);
    task.setDescription(description);
    task.setDueDate(dueDate);
    task.setAuthor(author);
    task.setStatus(status);

    assertEquals(id, task.getId());
    assertEquals(title, task.getTitle());
    assertEquals(description, task.getDescription());
    assertEquals(dueDate, task.getDueDate());
    assertEquals(author, task.getAuthor());
    assertEquals(status, task.getStatus());
  }

  @Test
  void testTaskStatusEnum() {
    assertNotNull(Task.TaskStatus.values());
    assertEquals(3, Task.TaskStatus.values().length);
    assertTrue(containsStatus(Task.TaskStatus.TODO));
    assertTrue(containsStatus(Task.TaskStatus.IN_PROGRESS));
    assertTrue(containsStatus(Task.TaskStatus.COMPLETED));
  }

  private boolean containsStatus(Task.TaskStatus status) {
    for (Task.TaskStatus s : Task.TaskStatus.values()) {
      if (s == status) {
        return true;
      }
    }
    return false;
  }
}
