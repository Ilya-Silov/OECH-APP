package com.example.oechapp.Controller;

import com.example.oechapp.Entity.RequestDto.CreateUserRequest;
import com.example.oechapp.Entity.RequestDto.Mapper.UserMapper;
import com.example.oechapp.Entity.ResponseDTO.UserResponse;
import com.example.oechapp.Entity.ResponseDTO.mappers.UserResponseMapper;
import com.example.oechapp.Entity.User;
import com.example.oechapp.Security.UserDetailsImpl;
import com.example.oechapp.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Контроллер для работы с пользователями")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserResponseMapper userResponseMapper;

    @Operation(summary = "Создать пользователя", description = "Создает нового пользователя.")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Parameter(description = "Данные для создания пользователя", required = true) @RequestBody CreateUserRequest user) {
        User createdUser = userMapper.mapCreateUserRequestToUser(user);
        createdUser = userService.createUser(createdUser);
        return new ResponseEntity<>(userResponseMapper.mapUserToUserResponse(createdUser), HttpStatus.CREATED);
    }

    @Operation(summary = "Получить всех пользователей", description = "Получает список всех пользователей.")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers().stream().map(userResponseMapper::mapUserToUserResponse).toList();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Получить текущего пользователя", description = "Получает текущего аутентифицированного пользователя.")
    @GetMapping("/current")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JWT Token")
    public ResponseEntity<?> getCurrent(Authentication auth)
    {
        if (auth!= null && auth.isAuthenticated())
        {
            UserDetailsImpl auser = (UserDetailsImpl) auth.getPrincipal();
            UserResponse userResponse = userService.getUserByEmail(auser.getUsername()).map(userResponseMapper::mapUserToUserResponse).get();
            return new ResponseEntity<>(userResponse,HttpStatus.OK);
        }
        return new ResponseEntity<>("You are not authenticated", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Получить пользователя по ID", description = "Получает детали пользователя на основе предоставленного ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@Parameter(description = "ID пользователя", required = true) @PathVariable Long id, Authentication auth) {
        if (auth != null)
        {
            UserDetailsImpl auser = (UserDetailsImpl) auth.getPrincipal();

            LoggerFactory.getLogger(this.getClass()).info(auser.getUsername());
        }
        else
        {
            LoggerFactory.getLogger(this.getClass()).info("пупупу");
        }
        return userService.getUserById(id)
                .map(user -> new ResponseEntity<>(userResponseMapper.mapUserToUserResponse(user), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Обновить данные пользователя", description = "Обновляет существующего пользователя. Для обновления требуется весь объект (все поля).")
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JWT Token")
    public ResponseEntity<UserResponse> updateUser(@Parameter(description = "ID пользователя", required = true) @PathVariable Long id, @Parameter(description = "Данные для обновления пользователя", required = true) @RequestBody CreateUserRequest user, Authentication auth) {
        UserDetailsImpl auser = (UserDetailsImpl) auth.getPrincipal();
        User usera = userService.getUserByEmail(auser.getUsername()).get();

        if (!id.equals(usera.getId()))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        User updatedUser = userService.updateUser(id, userMapper.mapCreateUserRequestToUser(user));
        if (updatedUser != null) {
            return new ResponseEntity<>(userResponseMapper.mapUserToUserResponse(updatedUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Обновить аватар пользователя", description = "Обновляет аватар пользователя.")
    @PutMapping("/{id}/avatar")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JWT Token")
    public ResponseEntity<UserResponse> updateAvatar(@Parameter(description = "ID пользователя", required = true) @PathVariable Long id, @Parameter(description = "Фото пользователя", required = true) @RequestParam MultipartFile photo, Authentication auth)
    {
        UserDetailsImpl auser = (UserDetailsImpl) auth.getPrincipal();
        User usera = userService.getUserByEmail(auser.getUsername()).get();

        if (!id.equals(usera.getId()))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            return new ResponseEntity<>(userResponseMapper.mapUserToUserResponse(userService.uploadPhoto(id, photo)), HttpStatus.OK);
        }
        catch (NoSuchElementException ex)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя на основе предоставленного ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID пользователя", required = true) @PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
