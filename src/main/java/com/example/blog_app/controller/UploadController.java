package com.example.blog_app.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
public class UploadController {

    @PostMapping("/upload-image")
    public Map<String, Object> uploadImage(@RequestParam("upload") MultipartFile file) throws IOException {
        String uploadDir = "uploads/";
        Files.createDirectories(Paths.get(uploadDir));

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Map<String, Object> response = new HashMap<>();
        response.put("url", "/uploads/" + fileName);
        return response;
    }
}
