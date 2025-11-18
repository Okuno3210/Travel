package com.example.demo.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Controller
public class SignupController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup"; // signup.html を表示
    }

    @PostMapping("/signup")
    public String signup(
            @ModelAttribute("user") User user,
            Model model) {

        // ユーザー名の重複チェック
        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "このユーザー名は既に使われています");
            return "signup"; // signup.html に戻る
        }

        userRepository.save(user);

        return "redirect:/login";
    }

}
