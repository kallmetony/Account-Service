package com.aaronr92.accountservice.config;

import com.aaronr92.accountservice.entities.BreachedPassword;
import com.aaronr92.accountservice.repositories.BreachedPasswordRepository;
import com.aaronr92.accountservice.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.time.Instant;
import java.util.*;

@Configuration
public class BeanConfig {

    @Autowired
    private UserService userService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(getEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    CommandLineRunner commandLineRunner(BreachedPasswordRepository breachedPasswordRepository) {
        return args -> {
            Set<String> breachedPasswords = new HashSet<>(
                    Set.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
                            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
                            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember")
            );

            breachedPasswords.forEach(pass -> breachedPasswordRepository.save(
                    BreachedPassword.builder()
                            .password(pass)
                            .build()));
        };
    }*/

    @Bean
    public AccessDeniedHandler getAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("timestamp", Instant.now().toString());
            data.put("status", HttpStatus.FORBIDDEN.value());
            data.put("error", "Forbidden");
            data.put("message", "Access Denied!");
            data.put("path", request.getRequestURI());
            response.getOutputStream()
                    .println(new ObjectMapper().writeValueAsString(data));
        };
    }
}
