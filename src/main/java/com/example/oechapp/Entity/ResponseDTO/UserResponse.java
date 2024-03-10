package com.example.oechapp.Entity.ResponseDTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class UserResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Double balance;

    private String photo;
}
