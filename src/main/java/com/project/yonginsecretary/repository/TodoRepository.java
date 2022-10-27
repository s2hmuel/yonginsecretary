package com.project.yonginsecretary.repository;

import com.project.yonginsecretary.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    public List<Todo> findTodoByUserId(Long userId);

    @Query(value = "select title from todo t where t.user_id = :id", nativeQuery = true)
    List<String> findByName(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "delete from todo t where t.user_id = :userId", nativeQuery = true)
    int deleteTodo(@Param("userId") Long userId);
}
