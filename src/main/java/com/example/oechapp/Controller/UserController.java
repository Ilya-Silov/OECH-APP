package com.example.oechapp.Controller;

import com.example.oechapp.Entity.RequestDto.CreateUserRequest;
import com.example.oechapp.Entity.RequestDto.Mapper.UserMapper;
import com.example.oechapp.Entity.User;
import com.example.oechapp.Service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest user) {
        User createdUser = userMapper.mapCreateUserRequestToUser(user);
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
    public ResponseEntity<User> getUserById(@PathVariable Long id, @AuthenticationPrincipal Principal auser) {
        if (auser != null)
        {
            LoggerFactory.getLogger(this.getClass()).info(auser.getName());
        }
        else
        {
            LoggerFactory.getLogger(this.getClass()).info("пупупу");
        }
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

    @PutMapping("/{id}/avatar")
    public ResponseEntity<User> udateAvatar(@PathVariable Long id, @RequestParam MultipartFile photo)
    {
        try {
            return new ResponseEntity<>(userService.uploadPhoto(id, photo), HttpStatus.OK);
        }
        catch (NoSuchElementException ex)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
