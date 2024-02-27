package com.example.oechapp.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Tracking")
@Getter
@Setter
public class Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_num")
    private String trackingNum;

    @ManyToOne
    @JoinColumn(name = "package_id")
    @JsonProperty("package")
    private Package _package;

    @Column(name = "status_date")
    private LocalDateTime statusDate;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TrackingStatus status;


    // Getters and setters
}
