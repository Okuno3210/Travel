package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/maplink", "/select", "/css/**", "/js/**", "/images/**","/h2-console/**","/USA","/EGY","/IND","/FRA","/ITA","/RUS","/AUS","/KOR","/CHN","/VNM","/CHE","/THA","/IRN","/signup").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(login -> login
                .loginPage("/login")                  // ← ここ追加：あなたの login.html を使う
                .loginProcessingUrl("/login")         // ← ここ追加：フォームの action と一致
                .usernameParameter("username")        // ← フォームinput name と一致
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .permitAll()
        )
        .csrf(csrf -> csrf
        		.ignoringRequestMatchers("/h2-console/**")
        )
        .headers(headers -> headers.frameOptions(frame -> frame.disable()));


        return http.build();
    }

    // パスワード暗号化用
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
