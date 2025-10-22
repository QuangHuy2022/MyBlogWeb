package com.example.blog_app.config;

import com.example.blog_app.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder; // Dùng plain text cho demo
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // ⚠️ Không nên dùng thật, chỉ dùng cho demo — sau nên thay bằng BCryptPasswordEncoder
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Cấu hình quyền truy cập
                .authorizeHttpRequests(auth -> auth
                        // Ai cũng có thể xem các trang chính và tài nguyên tĩnh
                        .requestMatchers(
                                "/", "/home", "/post/{id}", "/category/**",
                                "/css/**", "/js/**", "/images/**", "/uploads/**", "/webjars/**",
                                "/login", "/register", "/error", "/about", "/search","/blog"
                        ).permitAll()

                        // CRUD bài viết chỉ cho admin
                        .requestMatchers(
                                "/post/new", "/post/save", "/post/edit/**",
                                "/post/update/**", "/post/delete/**", "/categories/**"
                        ).hasRole("ADMIN")

                        // Các request còn lại yêu cầu đăng nhập
                        .anyRequest().authenticated()
                )


                // Cấu hình login form
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )

                // Cấu hình logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )

                // Cho phép truy cập ảnh & resource khi chưa login
                .authenticationProvider(authenticationProvider())

                // (Tuỳ chọn) Tắt CSRF cho demo nếu form chưa có token
                .csrf(csrf -> csrf.disable());


        return http.build();
    }
}
