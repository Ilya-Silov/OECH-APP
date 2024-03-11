package com.example.oechapp.Service;

import com.example.oechapp.Entity.Specials;
import com.example.oechapp.Repository.SpecialsRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SpecialsService {
    private final SpecialsRepository specialsRepository;
    private final FileStorageService fileStorageService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Specials> getSpecials()
    {
        return specialsRepository.findAll();
    }

    @PostConstruct
    private void fillSpecials()
    {
        logger.info("Заполнение акций...");

        if(!specialsRepository.findAll().isEmpty())
        {
            return;
        }
        Specials techMeetup = new Specials();
        techMeetup.setTitle("Tech Meetup coming soon");
        techMeetup.setPhoto(tryToLoadAvatar("https://s3-alpha-sig.figma.com/img/8894/b61f/d6ce7d2c61cade8725a0631e99a0521a?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=qWW4TJXZZAj7MIcB-eZVGCwYxkz8fXuua4SyktPG72et1DUCI4vxBJGZMc~7x1VZq46J1YzGgtOHIe1fMnMt4v8uUsTwrp95yJc~sL4rTh~B~CfEbK7Jv5eQisqrq8EkedZ4MKHpoTnAJSF5AcMhIYHZz72kaMuO0jJdErKqDJoQG7~74Yux9ngtJ4xBj96uEInjbXN0IVUkB~MctNsBgnFkBMhGOVuZfAitLmPP5SCJ0FFL4PoHqNv8tIbd17ahvr0xXlka-w5-S7854AXhyb-AkHrpoDUykagkGsR5Tbt1BXXn0LFiyj-CMW-irXOek1A7ebzZ6IlPArxsSGm5yA__"));

        Specials loveRevolution = new Specials();
        loveRevolution.setTitle("Love Revolution");
        loveRevolution.setPhoto(tryToLoadAvatar("https://s3-alpha-sig.figma.com/img/9551/b961/763cb3ef5d8c943030241eb0612c0226?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=PSha~M1BRsoCns44qb3tze-AN24TzQ0iP1LJZQVZyyxpZUrz6oFalW7JWEes0ZbhCa3YQ-ZTl6ri5ZhlIks3~iUWknKIN8MDVKEnF1mxjarF7K34HbQbvBO3wQYOOdEU782qupsxpO2kKPUu5UDPRQTjEGkeD2aFjMfY9NQ~DVzYGqo-hYCketL1ezTp5~nHy-GCK-M29KH8cu0KnY4GlEG5CgwbInPoKV7hg0GMNbQh1tDzHvsKRfqwmLaFxxcLQ0MvfK~cqY3GmZTYkMPRS-1XcpL037XNDl3UO5iUNuEn9kqxgR3GXMIyhj8Yc5Svdw5vEH~AdImxtDP-0RccsA__"));

        specialsRepository.saveAll(List.of(techMeetup, loveRevolution));
    }
    private String tryToLoadAvatar(String url)
    {
        try {
            Optional<String> photopath = fileStorageService.uploadPhotoFromURL(url);
            return photopath.orElse(null);
        } catch (IOException io) {
            logger.error("Не удалось загрузить фото - " + io.getMessage());
            return null;
        }
    }
}
