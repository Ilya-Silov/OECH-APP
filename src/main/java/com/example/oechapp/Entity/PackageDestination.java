package com.example.oechapp.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Package_destination")
@Getter
@Setter
public class PackageDestination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package _package;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Address destination;
}