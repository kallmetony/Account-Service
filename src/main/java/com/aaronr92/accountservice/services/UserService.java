package com.aaronr92.accountservice.services;

import com.aaronr92.accountservice.entities.Role;
import com.aaronr92.accountservice.entities.User;
import com.aaronr92.accountservice.exceptions.*;
import com.aaronr92.accountservice.repositories.BreachedPasswordRepository;
import com.aaronr92.accountservice.repositories.UserRepository;
import com.aaronr92.accountservice.util.RoleOperation;
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

    public User findByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email.toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    public User registerNewUser(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        checkValidPassword(user.getPassword());
        if (userRepository.findUserByEmailIgnoreCase(user.getEmail()).isEmpty()) {
            if (userRepository.count() == 0) {
                user.grantAuthority(Role.ROLE_ADMINISTRATOR);
            } else
                user.grantAuthority(Role.ROLE_USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        return user;
    }

    public ResponseEntity<List<User>> getAllRoles() {
        return ResponseEntity.ok((List<User>) userRepository.findAll());
    }

    public ResponseEntity<Map<String, String>> deleteUser(String email) {
        Optional<User> user = userRepository.findUserByEmailIgnoreCase(email);
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        if (user.get().getRoles().contains(Role.ROLE_ADMINISTRATOR))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        userRepository.delete(user.get());
        return ResponseEntity.ok(Map.of("user", email, "status", "Deleted successfully!"));
    }

    public ResponseEntity<Map<String, String>> changePassword(String newPassword, User authUser) {
        checkValidPassword(newPassword);
        checkDifferencePasswords(newPassword, authUser.getPassword());
        User tmpUser = userRepository.findUserByEmailIgnoreCase(authUser.getEmail()).get();
        tmpUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(tmpUser);
        return new ResponseEntity<>(Map.of("email", authUser.getEmail(),"status","The password has been updated successfully"), HttpStatus.OK);
    }

    public ResponseEntity<User> updateRole(RoleOperation operation) {
        Optional<User> user = userRepository.findUserByEmailIgnoreCase(operation.getEmail());
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        Role role = checkRole(operation.getRole());
        if (operation.getOperation().equals("GRANT")) {
            if (user.get().getRoles().contains(Role.ROLE_ADMINISTRATOR) || role == Role.ROLE_ADMINISTRATOR)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
            user.get().grantAuthority(role);
            userRepository.save(user.get());
        } else if (operation.getOperation().equals("REMOVE")) {
            if (!user.get().getRoles().contains(role))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
            if (role.equals(Role.ROLE_ADMINISTRATOR))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
            if (user.get().getRoles().size() == 1)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
            user.get().removeAuthority(role);
            userRepository.save(user.get());
        }
        return ResponseEntity.ok(user.get());
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
}
