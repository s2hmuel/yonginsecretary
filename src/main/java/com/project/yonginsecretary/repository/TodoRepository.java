package com.project.yonginsecretary.repository;

import com.project.yonginsecretary.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
