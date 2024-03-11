package com.example.oechapp.Controller;

import com.example.oechapp.Entity.RequestDto.ConfirmPackageRequest;
import com.example.oechapp.Entity.RequestDto.CreatePackageRequest;
import com.example.oechapp.Entity.RequestDto.Mapper.PackageMapper;
import com.example.oechapp.Entity.ResponseDTO.UserResponse;
import com.example.oechapp.Entity.User;
import com.example.oechapp.Security.UserDetailsImpl;
import com.example.oechapp.Service.PackageService;
import com.example.oechapp.Entity.Package;
import com.example.oechapp.Service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/packages")
@Tag(name = "Package", description = "Контроллер для взаимодействия с посылками")
public class PackageController {

    @Autowired
    private UserService userService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private PackageMapper packageMapper;

    @Operation(summary = "Создать посылку", description = "Создает новую посылку. При создании посылки, сумма списывается с баланса. При расчёте используется Instant Delivery = 500")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пакет успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createPackage(@Parameter(description = "Данные для создания посылки", required = true) @RequestBody CreatePackageRequest _package, Authentication auth) {
        UserDetailsImpl auser = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.getUserByEmail(auser.getUsername()).get();

        Package mapppedPackage = packageMapper.mapCreatePackageRequestToPackage(_package);
        mapppedPackage.setOwner(user);
        try {
            Package createdPackage = packageService.createPackage(mapppedPackage);
            return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
        }catch (IllegalArgumentException ex)
        {
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = "Получить все посылки пользователя", description = "Получает список всех посылок авторизованного пользователя.")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Package>> getAllPackages(Authentication auth) {
        UserDetailsImpl auser = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.getUserByEmail(auser.getUsername()).get();

        List<Package> packages = packageService.getUsersPackages(user);
        return new ResponseEntity<>(packages, HttpStatus.OK);
    }

    @Operation(summary = "Получить посылку по ID", description = "Получает детали посылки на основе предоставленного ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посылка найдена"),
            @ApiResponse(responseCode = "404", description = "Посылка не найдена")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Package> getPackageById(@Parameter(description = "ID посылки", required = true) @PathVariable Long id, Authentication auth) {
        Optional<Package> pack = packageService.getPackageById(id);

        UserDetailsImpl auser = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.getUserByEmail(auser.getUsername()).get();
        if (!Objects.equals(user.getId(), pack.get().getOwner().getId()))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return pack.map(pkg -> new ResponseEntity<>(pkg, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Обновить посылку", description = "Обновляет существующую посылку с учётом новых данных. Для обновления требуется весь объект (все поля).")
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Package> updatePackage(@Parameter(description = "ID посылки", required = true) @PathVariable Long id, @Parameter(description = "Данные для обновления посылки", required = true) @RequestBody CreatePackageRequest pkg, Authentication auth) {
        UserDetailsImpl auser = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.getUserByEmail(auser.getUsername()).get();

        Optional<Package> updatingPackage = packageService.getPackageById(id);
        if (updatingPackage.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!updatingPackage.get().getOwner().getId().equals(user.getId()))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(packageService.updatePackage(id, packageMapper.mapCreatePackageRequestToPackage(pkg)), HttpStatus.OK);
    }

    @Operation(summary = "Удалить посылку", description = "Удаляет существующую посылку на основе предоставленного ID.")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deletePackage(@Parameter(description = "ID посылки", required = true) @PathVariable Long id, Authentication auth) {
        UserDetailsImpl auser = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.getUserByEmail(auser.getUsername()).get();

        Optional<Package> deletingPackage = packageService.getPackageById(id);
        if (deletingPackage.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!deletingPackage.get().getOwner().getId().equals(user.getId()))
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        packageService.deletePackage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Подтвердить посылку", description = "Подтверждает посылку с рейтингом и необязательным комментарием.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посылка подтверждена"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Посылка не найдена")
    })
    @PutMapping("/{id}/confirm")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> confirmPackage(@Parameter(description = "ID посылки", required = true) @PathVariable Long id, @Parameter(description = "Данные для подтверждения посылки", required = true) @RequestBody ConfirmPackageRequest pkg, Authentication auth) {
        try {
            UserDetailsImpl auser = (UserDetailsImpl) auth.getPrincipal();
            User user = userService.getUserByEmail(auser.getUsername()).get();

            Optional<Package> updatingPackage = packageService.getPackageById(id);
            if (updatingPackage.isEmpty())
            {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (!updatingPackage.get().getOwner().getId().equals(user.getId()))
            {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            Package updatedPackage = packageService.confirmPackage(id, pkg.getRating(), pkg.getComment());
            return new ResponseEntity<>(updatedPackage, HttpStatus.OK);
        }
        catch (IllegalStateException ex)
        {
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (NotFoundException ex)
        {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
