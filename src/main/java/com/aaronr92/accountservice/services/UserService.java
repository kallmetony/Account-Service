package com.aaronr92.accountservice.services;

import com.aaronr92.accountservice.dto.RoleOperation;
import com.aaronr92.accountservice.dto.UserAccess;
import com.aaronr92.accountservice.entities.User;
import com.aaronr92.accountservice.exceptions.*;
import com.aaronr92.accountservice.repositories.BreachedPasswordRepository;
import com.aaronr92.accountservice.repositories.UserRepository;
import com.aaronr92.accountservice.util.Action;
import com.aaronr92.accountservice.util.Operation;
import com.aaronr92.accountservice.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BreachedPasswordRepository breachedPasswordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuditService auditService;

    private final int MAX_FAILED_ATTEMPTS = 5;

    public User registerNewUser(User user, String path) {
        user.setEmail(user.getEmail().toLowerCase());
        user.setNonLocked(true);
        checkValidPassword(user.getPassword());
        if (userRepository.findUserByEmailIgnoreCase(user.getEmail()).isEmpty()) {
            if (userRepository.count() == 0) {
                user.grantAuthority(Role.ROLE_ADMINISTRATOR);
            } else
                user.grantAuthority(Role.ROLE_USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            auditService.logEvent(Action.CREATE_USER, null, user.getEmail(), path);
            userRepository.save(user);
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        return user;
    }

    public ResponseEntity<List<User>> getAllRoles() {
        return ResponseEntity.ok((List<User>) userRepository.findAll());
    }

    public ResponseEntity<Map<String, String>> deleteUser(String email, String path, String adminEmail) {
        Optional<User> user = userRepository.findUserByEmailIgnoreCase(email);
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        if (user.get().getRoles().contains(Role.ROLE_ADMINISTRATOR))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");

        auditService.logEvent(Action.DELETE_USER, adminEmail, user.get().getEmail(), path);
        userRepository.delete(user.get());

        return ResponseEntity.ok(Map.of("user", email, "status", "Deleted successfully!"));
    }

    public ResponseEntity<Map<String, String>> changePassword(String newPassword, User authUser, String path) {
        checkValidPassword(newPassword);
        checkDifferencePasswords(newPassword, authUser.getPassword());
        User tmpUser = (User) loadUserByUsername(authUser.getEmail());
        tmpUser.setPassword(passwordEncoder.encode(newPassword));

        auditService.logEvent(Action.CHANGE_PASSWORD, tmpUser.getEmail(), tmpUser.getEmail(), path);
        userRepository.save(tmpUser);

        return new ResponseEntity<>(Map.of("email", authUser.getEmail(),"status","The password has been updated successfully"), HttpStatus.OK);
    }

    public ResponseEntity<User> updateRole(RoleOperation operation, String path , String adminEmail) {
        User user = (User) loadUserByUsername(operation.getEmail());
        Role role = checkRole(operation.getRole());

        switch (operation.getOperation()) {
            case "GRANT":
                if (user.getRoles().contains(Role.ROLE_ADMINISTRATOR) || role == Role.ROLE_ADMINISTRATOR)
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
                user.grantAuthority(role);

                String message = String.format("Grant role %s to %s", role.name().split("_")[1], user.getEmail());
                auditService.logEvent(Action.GRANT_ROLE, adminEmail, message, path);
            break;

            case "REMOVE":
                if (!user.getRoles().contains(role))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
                if (role.equals(Role.ROLE_ADMINISTRATOR))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
                if (user.getRoles().size() == 1)
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
                user.removeAuthority(role);

                message = String.format("Remove role %s from %s", role.name().split("_")[1], user.getEmail());
                auditService.logEvent(Action.REMOVE_ROLE, adminEmail, message, path);
            break;
        }
        return ResponseEntity.ok(userRepository.save(user));
    }

    public ResponseEntity<UserAccess> userAccessOperation(UserAccess operation, String adminEmail, String requestPath) {
        User user = (User) loadUserByUsername(operation.getUser());

        if (user.getRoles().contains(Role.ROLE_ADMINISTRATOR))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't lock the ADMINISTRATOR!");

        user.setNonLocked(Operation.LOCK != operation.getOperation());
        if (Operation.LOCK == operation.getOperation()) {
            auditService.logEvent(Action.LOCK_USER, user.getEmail(), String.format("Lock user %s", user.getEmail()), requestPath);
            operation.setStatus(String.format("User %s locked!", user.getEmail()));
        } else {
            user.setFailedAttempts(0);
            operation.setStatus(String.format("User %s unlocked!", user.getEmail()));
            auditService.logEvent(Action.UNLOCK_USER, adminEmail, String.format("Unlock user %s", user.getEmail()), requestPath);
        }
        userRepository.save(user);
        return ResponseEntity.ok(operation);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmailIgnoreCase(email);
        if (user.isPresent())
            return user.get();
        else
            throw new UsernameNotFoundException(String.format("Username[%s] not found", email));
    }

    private void checkValidPassword(String password) {
        if (password == null || password.length() < 12) {
            throw new PasswordTooShortException(Message.SIGNUP);
        }
        if (breachedPasswordRepository.existsBreachedPasswordsByPassword(password)) {
            throw new BreachedPasswordException();
        }
    }

    private void checkDifferencePasswords(String newPassword, String oldPassword) {
        if (passwordEncoder.matches(newPassword, oldPassword)) {
            throw new RepetitivePasswordException();
        }
    }

    private Role checkRole(String role) {
        for (Role r : Role.values()) {
            if (r.name().equals(String.format("ROLE_%s", role))) {
                return r;
            }
        }
        throw new RoleNotFoundException();
    }

    public void increaseFailedAttempts(User user, String path) {
        user.setFailedAttempts(user.getFailedAttempts() + 1);
        if (user.getFailedAttempts() > MAX_FAILED_ATTEMPTS)
            lockUser(user, path);
        userRepository.save(user);
    }

    private void lockUser(User user, String path) {
        user.setNonLocked(false);
        auditService.logEvent(Action.BRUTE_FORCE, user.getEmail(), path, path);
        auditService.logEvent(Action.LOCK_USER, user.getEmail(), String.format("Lock user %s", user.getEmail()), path);
    }

}
