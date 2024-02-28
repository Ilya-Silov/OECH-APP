package com.example.oechapp.Entity.RequestDto;

import lombok.Data;

@Data
public class ReplenishBalanceRequest {
    private Long userId;
    private Double amount;
}
