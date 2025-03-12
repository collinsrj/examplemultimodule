/*
 * Copyright 2024 Collins
 */
package com.collinsrj.exampleweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.collinsrj.exampleweb.model.Task;
import com.collinsrj.exampleweb.service.TaskService;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public Mono<String> listTasks(Model model) {
    return taskService
        .getAllTasks()
        .collectList()
        .doOnNext(tasks -> model.addAttribute("tasks", tasks))
        .thenReturn("tasks/list");
  }

  @GetMapping("/create")
  public Mono<String> showCreateForm(Model model) {
    model.addAttribute("task", new Task());
    model.addAttribute("statuses", Task.TaskStatus.values());
    return Mono.just("tasks/form");
  }

  @PostMapping
  public Mono<String> createTask(@ModelAttribute Task task) {
    return taskService.createTask(task).thenReturn("redirect:/tasks");
  }

  @GetMapping("/{id}/edit")
  public Mono<String> showEditForm(@PathVariable String id, Model model) {
    model.addAttribute("statuses", Task.TaskStatus.values());
    return taskService
        .getTaskById(id)
        .doOnNext(task -> model.addAttribute("task", task))
        .thenReturn("tasks/form");
  }

  @PostMapping("/{id}")
  public Mono<String> updateTask(@PathVariable String id, @ModelAttribute Task task) {
    return taskService.updateTask(id, task).thenReturn("redirect:/tasks");
  }

  @GetMapping("/{id}/delete")
  public Mono<String> deleteTask(@PathVariable String id) {
    return taskService.deleteTask(id).thenReturn("redirect:/tasks");
  }

  @GetMapping("/author/{author}")
  public Mono<String> listTasksByAuthor(@PathVariable String author, Model model) {
    model.addAttribute("currentAuthor", author);
    return taskService
        .getTasksByAuthor(author)
        .collectList()
        .doOnNext(tasks -> model.addAttribute("tasks", tasks))
        .thenReturn("tasks/list");
  }
}
