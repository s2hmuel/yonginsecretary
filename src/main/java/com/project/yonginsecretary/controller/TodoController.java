package com.project.yonginsecretary.controller;

import com.project.yonginsecretary.auth.PrincipalDetails;
import com.project.yonginsecretary.dto.TodoWriteDTO;
import com.project.yonginsecretary.entity.Todo;
import com.project.yonginsecretary.entity.User;
import com.project.yonginsecretary.repository.TodoRepository;
import com.project.yonginsecretary.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/todo")
public class TodoController {

    private final UserService userService;

    private final TodoRepository todoRepository;

    @GetMapping("/write")
    public String writeForm(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User loginUser = principalDetails.getUser();
        TodoWriteDTO todoWriteDTO = new TodoWriteDTO();
        todoWriteDTO.setWriter(loginUser.getNickname());
        model.addAttribute("todoWriteDTO", todoWriteDTO);
        return "todo/writeForm";
    }

    @PostMapping("/write")
    public String write(@Valid @ModelAttribute TodoWriteDTO todoWriteDTO, BindingResult bindingResult,
                        @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) throws IOException {
        User loginUser = principalDetails.getUser();

        if(bindingResult.hasErrors()) {
            log.info("글 작성 실패");
            todoWriteDTO.setWriter(loginUser.getNickname());
            return "todo/writeForm";
        }

        Todo todo = new Todo();
        todo.setTitle(todoWriteDTO.getTitle());
        todo.setUser(userService.findById(loginUser.getId()));
        todo.setUploadDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        todoRepository.save(todo);
        model.addAttribute("msg", "글 작성 성공!");
        model.addAttribute("url", "/");
        return "message";
    }
}
