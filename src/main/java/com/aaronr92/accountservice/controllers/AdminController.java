package com.aaronr92.accountservice.controllers;

import com.aaronr92.accountservice.entities.User;
import com.aaronr92.accountservice.services.UserService;
import com.aaronr92.accountservice.dto.RoleOperation;
import com.aaronr92.accountservice.dto.UserAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping("/user")
    ResponseEntity<List<User>> getAllRoles() {
        return userService.getAllRoles();
    }

    @DeleteMapping("/user/{email}")
    ResponseEntity<Map<String, String>> deleteUser(@PathVariable String email,
                                                   HttpServletRequest request,
                                                   @AuthenticationPrincipal User user) {
        return userService.deleteUser(email, request.getServletPath(), user.getEmail());
    }

    @PutMapping("/user/role")
    ResponseEntity<User> updateUserRoles(@RequestBody RoleOperation operation,
                                         HttpServletRequest request,
                                         @AuthenticationPrincipal User user) {
        return userService.updateRole(operation, request.getServletPath(), user.getEmail());
    }

    @PutMapping("/user/access")
    ResponseEntity<UserAccess> userAccess(@AuthenticationPrincipal User admin,
                                          @RequestBody UserAccess operation,
                                          HttpServletRequest request) {
        return userService.userAccessOperation(operation, admin.getEmail(), request.getServletPath());
    }
}
