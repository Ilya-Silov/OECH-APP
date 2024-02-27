package com.example.oechapp.Controller;

import com.example.oechapp.Entity.RequestDto.CreateUserRequest;
import com.example.oechapp.Entity.RequestDto.Mapper.UserMapper;
import com.example.oechapp.Entity.User;
import com.example.oechapp.Service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest user) {
        User createdUser = userMapper.mapCreateUserRequestToUser(user);
        createdUser.setPassword(passwordEncoder.encode(createdUser.getPassword()));
        createdUser = userService.createUser(createdUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    @PostConstruct
    private void fillUsers()
    {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("123");
        createUserRequest.setFirstName("asd");
        createUserRequest.setLastName("asd");
        createUserRequest.setPassword("123");
        createUser(createUserRequest);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody CreateUserRequest user) {
        User updatedUser = userService.updateUser(id, userMapper.mapCreateUserRequestToUser(user));
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
