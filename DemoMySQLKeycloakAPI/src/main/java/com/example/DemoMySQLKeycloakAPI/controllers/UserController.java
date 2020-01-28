package com.example.DemoMySQLKeycloakAPI.controllers;

import com.example.DemoMySQLKeycloakAPI.model.User;
import com.example.DemoMySQLKeycloakAPI.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasAuthority('SCOPE_keycloak-federation')")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
            this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers(@RequestParam(name = "username", required = false, defaultValue = "") String username,
                                  @RequestParam(name = "email", required = false, defaultValue = "") String email,
                                  @RequestParam(name = "exactSearch", required = false, defaultValue = "false") boolean exactSearch,
                                  @RequestParam(name = "min", required = false, defaultValue = "-1") String min,
                                  @RequestParam(name = "max", required = false, defaultValue = "-1") String max) {
        // exact username search
        if(exactSearch && !username.isEmpty()) {
            return List.of(userRepository.findByUsername(username));
        }

        // exact email search
        if(exactSearch && !email.isEmpty()) {
            return List.of(userRepository.findByEmail(email));
        }
        int minNum = parseInt(min);
        int maxNum = parseInt(max);
        if(minNum >= 0 && maxNum >= 0) {
            return userRepository.findByUsernameContainingAndEmailContaining(username, email, minNum, maxNum);
        }
        return userRepository.findByUsernameContainingAndEmailContaining(username, email);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody(required = false) User user) {
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsUserByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setId(null);
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") UUID id) {
        if(id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(!userRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") UUID id, @RequestBody(required = false) User userUpdate) {
        if(id == null || userUpdate == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        userRepository.save(user.updateUser(userUpdate));

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") UUID id) {
        if(id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<User>(userOptional.get(), HttpStatus.OK);
    }

    private int parseInt(String possibleNumber) {
        try{
            int num = Integer.parseInt(possibleNumber);
            return num;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
