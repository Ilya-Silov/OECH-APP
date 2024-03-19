package com.example.oechapp.Service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class RecoveryCodeService {

    private final Map<String, String> resetCodes = new HashMap<>(); // Хранилище для кодов сброса

    private String generateRandomNumericCode() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int code = random.nextInt(max - min + 1) + min;
        return String.valueOf(code);
    }

    public String getResetCode(String email) {
        String code = generateRandomNumericCode();
        resetCodes.put(email, code);
        return code;
    }

    public boolean verifyResetCode(String email, String code) {
        String savedCode = resetCodes.get(email);
        return savedCode != null && savedCode.equals(code);
    }
}
