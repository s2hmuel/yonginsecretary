package com.project.yonginsecretary.service;

import com.project.yonginsecretary.entity.Todo;
import com.project.yonginsecretary.repository.TodoRepository;
import com.project.yonginsecretary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public List<String> getTodo(long id) {

        return todoRepository.findByName(id);
    }

    public int deleteTodo(Long userId, String title) {

        System.out.println("print in service layer ### userId, title : " + userId + title);

        return todoRepository.deleteTodo(userId);

    }
}
