package com.example.backend;


import com.example.backend.security.User;
import com.example.backend.security.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
public class DbInitialization {

    private final AlunoRepository alunoRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DbInitialization(AlunoRepository alunoRepository, UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.alunoRepository = alunoRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner inicializarDados() {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User(
                        "admin",
                        passwordEncoder.encode("admin123"),
                        Set.of("ROLE_ADMIN", "ROLE_USER")
                );
                User user = new User(
                        "user",
                        passwordEncoder.encode("user123"),
                        Set.of("ROLE_USER")
                );
                userRepository.saveAll(List.of(admin, user));
            }
        };
    }
}