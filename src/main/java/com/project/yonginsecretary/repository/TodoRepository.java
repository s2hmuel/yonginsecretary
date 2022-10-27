package com.project.yonginsecretary.repository;

import com.project.yonginsecretary.entity.Todo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Page<Todo> findByUserId(Long id, Pageable pageable);

    Page<Todo> findAll(Pageable pageable);

    @Query(value = "select title from todo t where t.user_id = :id", nativeQuery = true)
    List<String> findByName(@Param("id") Long id);

    public void deleteByUserIdAndTitle(Long id, String title);

}
