package com.example.oechapp.Service;

import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${avatar.upload.directory}")
    private String UPLOAD_DIRECTORY;
    private final ResourceLoader resourceLoader;

    public Resource getFile(String fileName)
    {
        return resourceLoader.getResource("file:" + UPLOAD_DIRECTORY + "/" + fileName);
    }

    public Optional<String> uploadPhoto(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String filename = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIRECTORY, filename));
            return Optional.of("/files/"+filename);
        }
        return Optional.empty();
    }
    public Optional<String> uploadPhotoFromURL(String url) throws IOException {
        if (url != null && !url.isEmpty()) {
            String filename = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + "_downloaded.jpg";
            URL imageUrl = new URL(url);

            Files.copy(imageUrl.openStream(), Paths.get(UPLOAD_DIRECTORY, filename));
            return Optional.of("/files/" + filename);
        }
        return Optional.empty();
    }
}
