package com.example.oechapp.Entity.RequestDto.Mapper;

import com.example.oechapp.Entity.Address;
import com.example.oechapp.Entity.RequestDto.CreatePackageRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.example.oechapp.Entity.Package;

@Mapper(componentModel = "spring")
public interface PackageMapper {

    PackageMapper INSTANCE = Mappers.getMapper(PackageMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "origin", target = "originAddress")
    @Mapping(source = "destinations", target = "destinations")
    Package mapCreatePackageRequestToPackage(CreatePackageRequest request);


    // Добавьте другие методы маппинга, если необходимо
}
