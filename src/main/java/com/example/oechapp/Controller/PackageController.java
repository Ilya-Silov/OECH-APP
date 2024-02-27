package com.example.oechapp.Controller;

import com.example.oechapp.Entity.RequestDto.ConfirmPackageRequest;
import com.example.oechapp.Entity.RequestDto.CreatePackageRequest;
import com.example.oechapp.Entity.RequestDto.Mapper.PackageMapper;
import com.example.oechapp.Entity.User;
import com.example.oechapp.Service.PackageService;
import com.example.oechapp.Entity.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;
    @Autowired
    private PackageMapper packageMapper;

    @PostMapping
    public ResponseEntity<Package> createPackage(@RequestBody CreatePackageRequest _package) {

        Package mapppedPackage = packageMapper.mapCreatePackageRequestToPackage(_package);
        //mapppedPackage.setOwner(new User());
        Package createdPackage = packageService.createPackage(mapppedPackage);
        return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Package>> getAllPackages() {
        List<Package> packages = packageService.getAllPackages();
        return new ResponseEntity<>(packages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Package> getPackageById(@PathVariable Long id) {
        return packageService.getPackageById(id)
                .map(pkg -> new ResponseEntity<>(pkg, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Package> updatePackage(@PathVariable Long id, @RequestBody CreatePackageRequest pkg) {
        Package updatedPackage = packageService.updatePackage(id, packageMapper.mapCreatePackageRequestToPackage(pkg));
        if (updatedPackage != null) {
            return new ResponseEntity<>(updatedPackage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmPackage(@PathVariable Long id, @RequestBody ConfirmPackageRequest pkg) {
        try {
            Package updatedPackage = packageService.confirmPackage(id, pkg.getRating(), pkg.getComment());
            return new ResponseEntity<>(updatedPackage, HttpStatus.OK);
        }
        catch (IllegalStateException ex)
        {
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (NotFoundException ex)
        {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
