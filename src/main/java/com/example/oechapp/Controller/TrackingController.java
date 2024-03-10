package com.example.oechapp.Controller;
import com.example.oechapp.Entity.Tracking;
import com.example.oechapp.Entity.User;
import com.example.oechapp.Service.PackageService;
import com.example.oechapp.Service.TrackingService;
import com.example.oechapp.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracking")
@Tag(name = "Tracking", description = "Контроллер для отслеживания")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    @Operation(summary = "Получить отслеживание по номеру отслеживания", description = "Получает отслеживание по номеру отслеживания.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отслеживание найдено"),
            @ApiResponse(responseCode = "404", description = "Отслеживание не найдено")
    })
    @GetMapping("/{tracking-num}")
    public ResponseEntity<List<Tracking>> getTrackingByTrackingNum(@Parameter(description = "Номер отслеживания", required = true) @PathVariable("tracking-num") String trackingNum) {
        List<Tracking> tracking = trackingService.getTrackingByTrackingNum(trackingNum);

        if (tracking == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (tracking.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tracking, HttpStatus.OK);
    }
}