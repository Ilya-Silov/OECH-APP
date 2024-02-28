package com.example.oechapp.Service;

import com.example.oechapp.Entity.*;
import com.example.oechapp.Entity.Package;
import com.example.oechapp.Repository.AddressRepository;
import com.example.oechapp.Repository.PackageRaitingRepository;
import com.example.oechapp.Repository.PackageRepository;
import com.example.oechapp.Repository.TrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.webjars.NotFoundException;


@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private PackageRaitingRepository packageRaitingRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private TrackingService trackingService;
    private TransactionService transactionService;

    public Package createPackage(Package _package) {
        User user = new User();
        _package.setOwner(user);

        double cost = getPackageCost(_package);
        if (_package.getOwner().getBalance() - cost<0)
        {
            throw new IllegalArgumentException("Insufficient funds");
        }
        transactionService.debitBalance(cost, _package.getOwner().getId());

        addressRepository.save(_package.getOriginAddress());
        addressRepository.saveAll(_package.getDestinations());

        packageRepository.save(_package);

        _package.setTrackingNumber(trackingService.createTracking(_package));

        return packageRepository.save(_package);
    }

    public Package confirmPackage(Long id, int rating, String comment)
    {
        Optional<Package> optionalPackage = packageRepository.findById(id);
        if (!optionalPackage.isPresent()) {
            throw new NotFoundException("Package not found");
        }
        if (packageRaitingRepository.findByPackageId(id).isPresent())
        {
            throw new IllegalStateException("Package already confirmed");
        }
        Package pck = optionalPackage.get();
        PackageRatings ratings = new PackageRatings();
        ratings.set_package(pck);
        ratings.setRate(rating);
        ratings.setFeedback(comment);
        packageRaitingRepository.save(ratings);
        return pck;
    }

    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    public Optional<Package> getPackageById(Long id) {
        return packageRepository.findById(id);
    }

    public Package updatePackage(Long id, Package newPackage) {
        Optional<Package> optionalPackage = packageRepository.findById(id);
        if (optionalPackage.isPresent()) {
            Package existingPackage = optionalPackage.get();
            newPackage.setId(existingPackage.getId());
            addressRepository.save(newPackage.getOriginAddress());
            addressRepository.saveAll(newPackage.getDestinations());
            // Update existing package fields
            return packageRepository.save(newPackage);
        } else {
            // Handle package not found
            return null;
        }
    }

    public void deletePackage(Long id) {
        packageRepository.deleteById(id);
    }

    private double getPackageCost(Package pck)
    {
        return (500 + (2500 * pck.getDestinations().size())) * 1.05;
    }
}
