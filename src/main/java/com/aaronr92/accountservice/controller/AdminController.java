package com.aaronr92.accountservice.controller;

import com.aaronr92.accountservice.entity.BreachedPassword;
import com.aaronr92.accountservice.entity.User;
import com.aaronr92.accountservice.service.BreachedPasswordService;
import com.aaronr92.accountservice.service.UserService;
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

    @Autowired
    BreachedPasswordService breachedPasswordService;

    @GetMapping("/user")
    ResponseEntity<List<User>> getAllUsers() {
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

    @PostMapping("/breached-password")
    ResponseEntity<?> addBreachedPassword(@RequestParam String password) {
        return breachedPasswordService.addBreachedPassword(password);
    }

    @DeleteMapping("/breached-password")
    ResponseEntity<?> removeBreachedPassword(@RequestParam String password) {
        return breachedPasswordService.removeBreachedPassword(password);
    }
}
