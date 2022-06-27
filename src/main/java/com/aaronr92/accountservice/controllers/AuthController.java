package com.aaronr92.accountservice.controllers;

import com.aaronr92.accountservice.entities.User;
import com.aaronr92.accountservice.services.UserService;
import com.aaronr92.accountservice.util.NewUserPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    ResponseEntity<User> signUp(@RequestBody User user) {
        return new ResponseEntity<>(userService.registerNewUser(user), HttpStatus.OK);
    }

    @PostMapping("/changepass")
    ResponseEntity<Map<String, String>> changePassword(@AuthenticationPrincipal User userDetails,
                                              @Valid @RequestBody NewUserPassword newPassword) {
        return userService.changePassword(newPassword.getNewPassword(), userDetails);
    }
}
