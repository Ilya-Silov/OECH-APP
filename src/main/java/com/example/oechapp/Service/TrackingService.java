package com.example.oechapp.Service;

import com.example.oechapp.Entity.Package;
import com.example.oechapp.Entity.Tracking;
import com.example.oechapp.Entity.TrackingStatus;
import com.example.oechapp.Repository.TrackingRepository;
import com.example.oechapp.Repository.TrackingStatusRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TrackingService {

    @Autowired
    private TrackingRepository trackingRepository;
    @Autowired
    private TrackingStatusRepository trackingStatusRepository;

    public List<Tracking> getTrackingByTrackingNum(String trackingNum) {
        return trackingRepository.findAllByTrackingNum(trackingNum);
    }

    public String createTracking(Package pck)
    {
        List<Tracking> trackings = new ArrayList<>();
        String trackingNum = generateTrackingNumber();

        for (long i = 0; i < 4; i++) {
            Tracking tracking = new Tracking();
            tracking.set_package(pck);
            tracking.setTrackingNum(trackingNum);

            TrackingStatus status = trackingStatusRepository.findById(i+1).orElseGet(() -> trackingStatusRepository.save(new TrackingStatus()));

            tracking.setStatus(status);
            tracking.setStatusDate(LocalDateTime.now().plusHours((i-1)*3));
            trackings.add(tracking);
        }

        trackingRepository.saveAll(trackings);

        return trackingNum;
    }

    private String generateTrackingNumber() {
        // Получаем текущую дату и время
        LocalDateTime now = LocalDateTime.now();

        // Форматируем текущую дату и время в строку (например, "2024-02-27-12-30-45")
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));

        // Генерируем случайное число от 1000 до 9999
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;

        // Комбинируем дату-время и случайное число для создания уникального tracking number
        String trackingNumber ="R-" + formattedDateTime + "-" + randomNumber;

        return trackingNumber;
    }

    @PostConstruct
    private void fillStatuses()
    {
        List<TrackingStatus> trackingStatuses = new ArrayList<>();

        trackingStatuses.add(new TrackingStatus("Упаковано"));
        trackingStatuses.add(new TrackingStatus("В пути"));
        trackingStatuses.add(new TrackingStatus("Доставлено"));
        trackingStatusRepository.saveAll(trackingStatuses);
    }
}
