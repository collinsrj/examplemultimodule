/*
 * Copyright 2024 Collins
 */
package com.collinsrj.exampleweb.model;

import java.time.LocalDateTime;

public class Task {
  private String id;
  private String title;
  private String description;
  private LocalDateTime dueDate;
  private String author;
  private TaskStatus status;

  public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    COMPLETED
  }

  // Getters and Setters
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDateTime dueDate) {
    this.dueDate = dueDate;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }
}
