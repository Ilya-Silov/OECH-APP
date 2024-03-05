package com.example.oechapp.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Specials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String photo;
}