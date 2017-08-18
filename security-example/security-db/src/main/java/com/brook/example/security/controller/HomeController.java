package com.brook.example.security.controller;

import com.brook.example.security.domain.UserEntity;
import com.brook.example.security.service.UserService;
import java.security.Principal;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class HomeController {

    private final UserService userService;

    @GetMapping({"/", "/index", "/home"})
    public String root(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal Principal principal
        , Model model){
      model.addAttribute("username", principal.getName());
      return "user/user";
    }
    @PostMapping("/register")
    public String doRegister(UserEntity userEntity){
        // 此处省略校验逻辑
      try {
        userService.insert(userEntity);
        return "redirect:register?success";
      }catch (IllegalArgumentException e){
        return "redirect:register?error";
      }

    }

}