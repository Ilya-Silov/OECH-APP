package com.example.oechapp.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Tracking_statuses")
@Getter
@Setter
@NoArgsConstructor
public class TrackingStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public TrackingStatus(String name) {
        this.name = name;
    }

    private String name;
}