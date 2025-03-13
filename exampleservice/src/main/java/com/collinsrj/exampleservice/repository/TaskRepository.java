/*
                                * Copyright 2024 Collins
                                */
package com.collinsrj.exampleservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.collinsrj.exampleservice.model.Task;

import reactor.core.publisher.Flux;

@Repository
public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
  Flux<Task> findByAuthor(String author);
}
