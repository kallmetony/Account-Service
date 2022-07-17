package com.aaronr92.accountservice.controller;

import com.aaronr92.accountservice.entity.User;
import com.aaronr92.accountservice.service.UserService;
import com.aaronr92.accountservice.dto.NewUserPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    ResponseEntity<User> signUp(@Valid @RequestBody User user,
                                HttpServletRequest request) {
        return new ResponseEntity<>(userService.registerNewUser(user, request.getServletPath()), HttpStatus.OK);
    }

    @PostMapping("/changepass")
    ResponseEntity<Map<String, String>> changePassword(@AuthenticationPrincipal User userDetails,
                                                       @Valid @RequestBody NewUserPassword newPassword,
                                                       HttpServletRequest request) {
        return userService.changePassword(newPassword.getNewPassword(), userDetails, request.getServletPath());
    }
}
