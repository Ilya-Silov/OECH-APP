package com.example.oechapp.Service;

import com.example.oechapp.Entity.Specials;
import com.example.oechapp.Repository.SpecialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SpecialsService {
    private final SpecialsRepository specialsRepository;

    public List<Specials> getSpecials()
    {
        return specialsRepository.findAll();
    }
}
