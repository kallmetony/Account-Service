package com.aaronr92.accountservice.security;

import com.aaronr92.accountservice.entities.User;
import com.aaronr92.accountservice.repositories.UserRepository;
import com.aaronr92.accountservice.services.AuditService;
import com.aaronr92.accountservice.services.UserService;
import com.aaronr92.accountservice.util.Action;
import com.aaronr92.accountservice.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    AuditService auditService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            String username = new String(Base64.getDecoder().decode(authorization.split("\\s+")[1])).split(":")[0];
            String path = request.getServletPath();

            if (userRepository.findUserByEmailIgnoreCase(username).isEmpty())
                auditService.logEvent(Action.LOGIN_FAILED, username, path, path);
            else {
                User user = userRepository.findUserByEmailIgnoreCase(username).get();
                if (user.isAccountNonLocked()) {
                    auditService.logEvent(Action.LOGIN_FAILED, user.getEmail(), path, path);
                    if (!user.getRoles().contains(Role.ROLE_ADMINISTRATOR))
                        userService.increaseFailedAttempts(user, path);
                }
            }
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
