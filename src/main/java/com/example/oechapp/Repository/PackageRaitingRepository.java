package com.example.oechapp.Repository;

import com.example.oechapp.Entity.Package;
import com.example.oechapp.Entity.PackageRatings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PackageRaitingRepository extends JpaRepository<PackageRatings, Long> {
    @Query("SELECT pr from PackageRatings pr where pr._package.id = :id")
    public Optional<PackageRatings> findByPackageId(Long id);
}
