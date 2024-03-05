package com.example.oechapp.Controller;

import com.example.oechapp.Service.SpecialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/specials")
@RequiredArgsConstructor
public class SpecialsController {
    private final SpecialsService specialsService;
    @GetMapping
    public ResponseEntity<?> getSepcials()
    {
        return new ResponseEntity<>(specialsService.getSpecials(), HttpStatus.OK);
    }
}
