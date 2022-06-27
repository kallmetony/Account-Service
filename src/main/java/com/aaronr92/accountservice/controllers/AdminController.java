package com.aaronr92.accountservice.controllers;

import com.aaronr92.accountservice.entities.User;
import com.aaronr92.accountservice.services.UserService;
import com.aaronr92.accountservice.util.RoleOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<Map<String, String>> deleteUser(@PathVariable String email) {
        return userService.deleteUser(email);
    }

    @PutMapping("/user/role")
    ResponseEntity<User> updateUserRoles(@RequestBody RoleOperation operation) {
        return userService.updateRole(operation);
    }
}
