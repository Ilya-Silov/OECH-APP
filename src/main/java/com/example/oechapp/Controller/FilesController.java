package com.example.oechapp.Controller;

import com.example.oechapp.Service.FileStorageService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
@Hidden
public class FilesController {

    private final FileStorageService fileStorageService;
    @GetMapping("/{filename:.+}")
    public ResponseEntity<InputStreamResource> getPhoto(@PathVariable String filename, HttpServletRequest request) throws IOException {
       Resource resource = fileStorageService.getFile(filename);
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

       return ResponseEntity.ok()
               .contentLength(resource.contentLength())
               .contentType(MediaType.parseMediaType(contentType))
               .body(new InputStreamResource(resource.getInputStream()));
    }
}
