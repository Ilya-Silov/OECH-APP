package com.example.oechapp.Controller;
import com.example.oechapp.Entity.Tracking;
import com.example.oechapp.Entity.User;
import com.example.oechapp.Service.PackageService;
import com.example.oechapp.Service.TrackingService;
import com.example.oechapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    @GetMapping("/{tracking-num}")
    public ResponseEntity<List<Tracking>> getTrackingByTrackingNum(@PathVariable("tracking-num") String trackingNum) {
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