package com.aaronr92.accountservice.service;

import com.aaronr92.accountservice.entity.BreachedPassword;
import com.aaronr92.accountservice.exception.Message;
import com.aaronr92.accountservice.repository.BreachedPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class BreachedPasswordService {

    @Autowired
    private BreachedPasswordRepository passwordRepository;

    public ResponseEntity<Map<String, String>> addBreachedPassword(String breachedPassword) {
        if (passwordRepository.existsBreachedPasswordsByPassword(breachedPassword))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This password is already saved");

        passwordRepository.save(BreachedPassword.builder().password(breachedPassword).build());
        return ResponseEntity.ok(Map.of("status", Message.ADD_BREACHED_PASSWORD.getMessage()));
    }

    public ResponseEntity<Map<String, String>> removeBreachedPassword(String breachedPassword) {
        if (!passwordRepository.existsBreachedPasswordsByPassword(breachedPassword))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No password found!");

        passwordRepository.delete(passwordRepository.findByPassword(breachedPassword));
        return ResponseEntity.ok(Map.of("status", Message.REMOVE_BREACHED_PASSWORD.getMessage()));
    }
}
