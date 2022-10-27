package com.project.yonginsecretary.service;

import com.project.yonginsecretary.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public List<String> getTodo(long id) {
        return todoRepository.findByName(id);
    }

    public void deleteTodo(long id, String title) {
        todoRepository.deleteByUserIdAndTitle(id, title);
    }
}
