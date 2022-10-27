package com.project.yonginsecretary.controller;

import com.project.yonginsecretary.auth.PrincipalDetails;
import com.project.yonginsecretary.dto.TodoWriteDTO;
import com.project.yonginsecretary.entity.Todo;
import com.project.yonginsecretary.entity.User;
import com.project.yonginsecretary.repository.TodoRepository;
import com.project.yonginsecretary.service.TodoService;
import com.project.yonginsecretary.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/todo")
public class TodoController {

    private final UserService userService;
    private final TodoRepository todoRepository;

    @GetMapping("/write")
    public String writeForm(Model model, @RequestParam(required = false, defaultValue = "1") int page, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User loginUser = principalDetails.getUser();
        TodoWriteDTO todoWriteDTO = new TodoWriteDTO();
        todoWriteDTO.setWriter(loginUser.getNickname());
        model.addAttribute("todoWriteDTO", todoWriteDTO);

        List<String> todoList = todoService.getTodo(loginUser.getId());

        model.addAttribute("todos", todoList);

        return "todo/writeForm";
    }

    @GetMapping("/chat")
    public String todoChat(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return "todo/chatbot";
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

//    @GetMapping("/write")
//    public String showtodoList(Model model, @RequestParam(required = false, defaultValue = "1") int page,
//                               @AuthenticationPrincipal PrincipalDetails principalDetails){
//
//        User loginUser = principalDetails.getUser();
//        PageRequest pageRequest = PageRequest.of(page - 1,10);
//        Page<Todo> result;
//
//        result = todoRepository.findByUserId(loginUser.getId(),pageRequest);
//        System.out.println(result);
//
//        model.addAttribute("todoList", result.getContent());
//        if(result.getTotalPages() == 0) {
//            model.addAttribute("totalPages", 1);
//        } else {
//            model.addAttribute("totalPages", result.getTotalPages());
//        }
//        model.addAttribute("hasNextPage", result.hasNext());
//        model.addAttribute("hasPreviousPage", result.hasPrevious());
//        model.addAttribute("page", page);
//
//        return "todo/write";
//    }

    private final TodoService todoService;

    @ResponseBody
    @GetMapping("/getTodo")
    public HashMap<String, Object> getTodo(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User loginUser = principalDetails.getUser();

        List<String> todoList = todoService.getTodo(loginUser.getId());

        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        resultMap.put("todo", todoList);

        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteTodo/{title}", method = {RequestMethod.DELETE})
    public void deleteChatbotTodo(@PathVariable String title, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("Delete todo : " + title);
        User loginUser = principalDetails.getUser();

        todoService.deleteTodo(loginUser.getId(), title);


    }
}
