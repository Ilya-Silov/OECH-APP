package com.example.oechapp.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Package")
@Getter
@Setter
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "origin_address_id")
    private Address originAddress;


    @ManyToMany
    @JoinTable(
            name = "package_destination",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "destination_id")
    )
    private List<Address> destinations;

    @Column(name = "package_items")
    private String packageItems;

    private Integer worth;

    @Column(name = "tracking_number")
    private String trackingNumber;

    private Integer weight;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
