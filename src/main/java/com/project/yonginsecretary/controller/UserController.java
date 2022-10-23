package com.project.yonginsecretary.controller;

import com.project.yonginsecretary.auth.PrincipalDetails;
import com.project.yonginsecretary.dto.SignInDTO;
import com.project.yonginsecretary.dto.SignUpDTO;
import com.project.yonginsecretary.dto.UserEditDTO;
import com.project.yonginsecretary.entity.User;
import com.project.yonginsecretary.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
@Slf4j
public class UserController {

    public static Hashtable sessionList = new Hashtable();
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute SignUpDTO signUpDTO) {
        return "user/signupForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignUpDTO signUpDTO, BindingResult bindingResult, Model model) {
        if (userService.findByLoginId(signUpDTO.getLoginId()).isPresent()) {
            bindingResult.rejectValue("loginId", "signupFail", "아이디가 중복됩니다!");
        }
        if (userService.findByNickname(signUpDTO.getNickname()).isPresent()) {
            bindingResult.rejectValue("nickname", "signupFail", "닉네임이 중복됩니다!");
        }
        if (!signUpDTO.getPassword().equals(signUpDTO.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "signupFail", "비밀번호가 일치하지 않습니다!");
        }
        if (bindingResult.hasErrors()) {
            log.info("회원가입 실패");
            return "user/signupForm";
        }

        User user = new User();
        user.setLoginId(signUpDTO.getLoginId());
        user.setNickname(signUpDTO.getNickname());
        user.setPassword( bCryptPasswordEncoder.encode(signUpDTO.getPassword()) );
        user.setRole("USER");
        user.setLikeCount(0);
        userService.signup(user);
        log.info("회원가입 성공 : {}", user.getNickname());

        model.addAttribute("msg", "회원가입에 성공했습니다! 로그인해주세요!");
        model.addAttribute("url", "/");
        return "message";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute SignInDTO signInDTO) {
        return "user/loginForm";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            sessionList.remove(session.getId());
            session.invalidate();
        }
        log.info("로그아웃 성공");
        model.addAttribute("msg", "로그아웃 되었습니다!");
        model.addAttribute("url", "/");
        return "message";
    }

    @GetMapping("/edit")
    public String editForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        User loginUser = principalDetails.getUser();
        UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setLoginId(loginUser.getLoginId());
        userEditDTO.setOldNickname(loginUser.getNickname());
        model.addAttribute("userEditDTO", userEditDTO);
        return "user/editForm";
    }

    @PostMapping("/edit")
    public String edit(@AuthenticationPrincipal PrincipalDetails principalDetails,
                       @Valid @ModelAttribute UserEditDTO userEditDTO, BindingResult bindingResult,
                       HttpServletRequest request, Model model) {
        User loginUser = principalDetails.getUser();
        if (!bCryptPasswordEncoder.matches(userEditDTO.getOldPassword(), loginUser.getPassword())) {
            bindingResult.rejectValue("oldPassword", "editFail", "현재 비밀번호를 틀렸습니다!");
        }
        if (!userEditDTO.getPassword().equals(userEditDTO.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "editFail", "비밀번호가 일치하지 않습니다!");
        }
        if (userService.findByNickname(userEditDTO.getNickname()).isPresent() && !loginUser.getNickname().equals(userEditDTO.getNickname())) {
            bindingResult.rejectValue("nickname", "editFail", "닉네임이 중복됩니다!");
        }

        if (bindingResult.hasErrors()) {
            log.info("정보수정 실패");
            return "user/editForm";
        }

        User user = userService.findById(loginUser.getId());

        user.setNickname(userEditDTO.getNickname());
        user.setPassword(bCryptPasswordEncoder.encode(userEditDTO.getPassword()));
        userService.edit(user);
        log.info("정보수정 성공 : {}", user.getNickname());
        HttpSession session = request.getSession(false);
        if (session != null) {
            sessionList.remove(session.getId());
            session.invalidate();
        }

        model.addAttribute("msg", "정보수정 성공! 다시 로그인 해주세요!");
        model.addAttribute("url", "/user/login");
        return "message";
    }

    @GetMapping("/list")
    public String userList(Model model, @RequestParam(required = false, defaultValue = "1") int page) {
        model.addAttribute("users", userService.findAll(page - 1));
        if (page == 1) {
            model.addAttribute("hasPreviousPage", false);
        } else {
            model.addAttribute("hasPreviousPage", true);
        }

        if (userService.findAll(page).isEmpty()) {
            model.addAttribute("hasNextPage", false);
        } else {
            model.addAttribute("hasNextPage", true);
        }
        model.addAttribute("lastPage", userService.findLastPage());
        model.addAttribute("nowPage", page);

        return "user/userList";
    }

    @GetMapping("/delete")
    public String deleteForm(@ModelAttribute SignUpDTO signUpDTO, Model model,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User loginUser = principalDetails.getUser();
        return "user/deleteForm";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute SignUpDTO signUpDTO, Model model, HttpServletRequest request,
                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User loginUser = principalDetails.getUser();
        if (!bCryptPasswordEncoder.matches(signUpDTO.getPassword(), loginUser.getPassword())) {
            model.addAttribute("msg", "비밀번호가 틀렸습니다!");
            model.addAttribute("url", "/user/delete");
            log.info("회원탈퇴 실패");
            return "message";
        }

        userService.delete(loginUser.getId());
        log.info("회원탈퇴 성공");
        HttpSession session = request.getSession(false);
        if (session != null) {
            sessionList.remove(session.getId());
            session.invalidate();
        }
        model.addAttribute("msg", "회원탈퇴 되었습니다!");
        model.addAttribute("url", "/");
        return "message";
    }

    @PostConstruct
    public void init() {
        User admin = new User();
        admin.setLoginId("admin");
        admin.setNickname("관리자");
        admin.setRole("ADMIN");
        admin.setPassword( bCryptPasswordEncoder.encode("1234") );
        admin.setLikeCount(0);
        userService.signup(admin);

        User user1 = new User();
        user1.setLoginId("test");
        user1.setNickname("테스트");
        user1.setRole("USER");
        user1.setPassword( bCryptPasswordEncoder.encode("1234") );
        user1.setLikeCount(0);
        userService.signup(user1);
    }
}