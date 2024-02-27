package com.example.oechapp.Repository;

import com.example.oechapp.Entity.TrackingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackingStatusRepository extends JpaRepository<TrackingStatus, Long> {
}
