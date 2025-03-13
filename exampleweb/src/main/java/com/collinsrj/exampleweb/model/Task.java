/*
                                * Copyright 2024 Collins
                                */
package com.collinsrj.exampleweb.model;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
}
