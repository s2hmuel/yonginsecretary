package com.project.yonginsecretary.controller;

import com.project.yonginsecretary.auth.PrincipalDetails;
import com.project.yonginsecretary.dto.TodoWriteDTO;
import com.project.yonginsecretary.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return "home";
        } else {
            User loginUser = principalDetails.getUser();
            model.addAttribute("loginUser", loginUser);
            return "home";
        }
    }

    @GetMapping("/message")
    public String message(@RequestParam String msg, String url, Model model) {
        model.addAttribute("msg", msg);
        model.addAttribute("url", url);
        return "message";
    }

}

