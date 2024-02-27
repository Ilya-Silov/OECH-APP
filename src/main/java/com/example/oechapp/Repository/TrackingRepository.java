package com.example.oechapp.Repository;

import com.example.oechapp.Entity.Tracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface TrackingRepository extends JpaRepository<Tracking, Long> {

    public List<Tracking> findAllByTrackingNum(String trackingNum);
}
