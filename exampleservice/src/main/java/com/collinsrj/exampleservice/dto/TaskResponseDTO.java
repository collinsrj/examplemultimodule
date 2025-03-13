/*
                                * Copyright 2024 Collins
                                */
package com.collinsrj.exampleservice.dto;

import java.time.LocalDateTime;

import com.collinsrj.exampleservice.model.Task;
import com.collinsrj.exampleservice.model.Task.TaskStatus;

public record TaskResponseDTO(
    String id,
    String title,
    String description,
    LocalDateTime dueDate,
    String author,
    TaskStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
  public static TaskResponseDTO fromEntity(Task task) {
    return new TaskResponseDTO(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        task.getDueDate(),
        task.getAuthor(),
        task.getStatus(),
        task.getCreatedAt(),
        task.getUpdatedAt());
  }
}
