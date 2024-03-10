package com.example.oechapp.Controller;

import com.example.oechapp.Service.SpecialsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/specials")
@RequiredArgsConstructor
@Tag(name = "Specials", description = "Котроллер для работы с рекламой")
public class SpecialsController {
    private final SpecialsService specialsService;

    @Operation(summary = "Получить все рекламы", description = "Получает список всех реклам.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список реклам получен"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<?> getSepcials()
    {
        return new ResponseEntity<>(specialsService.getSpecials(), HttpStatus.OK);
    }
}
