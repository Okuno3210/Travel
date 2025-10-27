package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Favorite;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserRepository userRepository;

    // お気に入り登録
    @PostMapping("/favorites/add/{countryId}")
    public String addFavorite(@PathVariable Long countryId, Principal principal) {
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            favoriteService.addFavorite(user, countryId);
        }
        return "redirect:/list"; // /list にリダイレクト
    }

    // お気に入り削除
    @PostMapping("/favorites/remove/{countryId}")
    public String removeFavorite(@PathVariable Long countryId, Principal principal) {
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            favoriteService.removeFavorite(user, countryId);
        }
        return "redirect:/list"; // /list にリダイレクト
    }

    // お気に入り一覧（/list で直接アクセス可能）
    @GetMapping("/list")
    public String favoritesList(Model model, Principal principal) {
        List<Favorite> favorites;
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            favorites = favoriteService.getFavorites(user);
        } else {
            favorites = List.of(); // 空リスト
        }
        model.addAttribute("favorites", favorites);
        return "list"; // list.html を表示
    }
}
