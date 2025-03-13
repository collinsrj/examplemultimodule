/*
                                * Copyright 2024 Collins
                                */
package com.collinsrj.exampleservice.dto;

import java.time.LocalDateTime;

import com.collinsrj.exampleservice.model.Task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskDTO {
  @NotBlank(message = "Title is required")
  private String title;

  @NotBlank(message = "Description is required")
  private String description;

  private LocalDateTime dueDate;

  @NotBlank(message = "Author is required")
  private String author;

  @NotNull(message = "Status is required")
  private Task.TaskStatus status;

  // Getters and Setters
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

  public Task.TaskStatus getStatus() {
    return status;
  }

  public void setStatus(Task.TaskStatus status) {
    this.status = status;
  }

  // Convert DTO to Entity
  public Task toEntity() {
    Task task = new Task();
    task.setTitle(this.title);
    task.setDescription(this.description);
    task.setDueDate(this.dueDate);
    task.setAuthor(this.author);
    task.setStatus(this.status);
    return task;
  }
}
