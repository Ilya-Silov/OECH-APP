package com.example.oechapp.Controller;

import com.example.oechapp.Entity.RequestDto.ConfirmPackageRequest;
import com.example.oechapp.Entity.RequestDto.CreatePackageRequest;
import com.example.oechapp.Entity.RequestDto.Mapper.PackageMapper;
import com.example.oechapp.Entity.User;
import com.example.oechapp.Service.PackageService;
import com.example.oechapp.Entity.Package;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@Tag(name = "Package", description = "Контроллер для взаимодействия с посылками")
public class PackageController {

    @Autowired
    private PackageService packageService;
    @Autowired
    private PackageMapper packageMapper;

    @Operation(summary = "Создать пакет", description = "Создает новый пакет.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пакет успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<Package> createPackage(@Parameter(description = "Данные для создания пакета", required = true) @RequestBody CreatePackageRequest _package) {

        Package mapppedPackage = packageMapper.mapCreatePackageRequestToPackage(_package);
        //mapppedPackage.setOwner(new User());
        Package createdPackage = packageService.createPackage(mapppedPackage);
        return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
    }

    @Operation(summary = "Получить все пакеты", description = "Получает список всех пакетов.")
    @GetMapping
    public ResponseEntity<List<Package>> getAllPackages() {
        List<Package> packages = packageService.getAllPackages();
        return new ResponseEntity<>(packages, HttpStatus.OK);
    }

    @Operation(summary = "Получить пакет по ID", description = "Получает детали пакета на основе предоставленного ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пакет найден"),
            @ApiResponse(responseCode = "404", description = "Пакет не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Package> getPackageById(@Parameter(description = "ID пакета", required = true) @PathVariable Long id) {
        return packageService.getPackageById(id)
                .map(pkg -> new ResponseEntity<>(pkg, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Обновить пакет", description = "Обновляет существующий пакет новыми данными. Для обновления требуется весь объект (все поля).")
    @PutMapping("/{id}")
    public ResponseEntity<Package> updatePackage(@Parameter(description = "ID пакета", required = true) @PathVariable Long id, @Parameter(description = "Данные для обновления пакета", required = true) @RequestBody CreatePackageRequest pkg) {
        Package updatedPackage = packageService.updatePackage(id, packageMapper.mapCreatePackageRequestToPackage(pkg));
        if (updatedPackage != null) {
            return new ResponseEntity<>(updatedPackage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Удалить пакет", description = "Удаляет существующий пакет на основе предоставленного ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@Parameter(description = "ID пакета", required = true) @PathVariable Long id) {
        packageService.deletePackage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Подтвердить пакет", description = "Подтверждает пакет с рейтингом и необязательным комментарием.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пакет подтвержден"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Пакет не найден")
    })
    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmPackage(@Parameter(description = "ID пакета", required = true) @PathVariable Long id, @Parameter(description = "Данные для подтверждения пакета", required = true) @RequestBody ConfirmPackageRequest pkg) {
        try {
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
