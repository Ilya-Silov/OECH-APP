package com.example.oechapp.Entity.RequestDto;

import com.example.oechapp.Entity.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CreatePackageRequest {
    private Address origin;
    private List<Address> destinations;
    @JsonProperty("package_items")
    private String packageItems;
    private int weight;
    private int worth;
}
