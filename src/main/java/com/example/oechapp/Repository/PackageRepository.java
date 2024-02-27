package com.example.oechapp.Repository;

import com.example.oechapp.Entity.Tracking;
import com.example.oechapp.Entity.User;
import com.example.oechapp.Entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface PackageRepository extends JpaRepository<Package, Long> {
    // здесь вы можете добавить дополнительные методы для работы с сущностью посылки
}

