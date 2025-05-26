package com.example.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String UPLOAD_DIR = "uploads/";

    public String storeFile(MultipartFile file, String subDirectory) throws IOException {
        // Create the upload path
        Path uploadPath = Paths.get(UPLOAD_DIR + subDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ?
                originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        String filename = UUID.randomUUID().toString() + extension;

        // Save file
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        // Return the relative path
        return "/uploads/" + subDirectory + filename;
    }

    public void deleteFile(String filePath) throws IOException {
        if (filePath != null && filePath.startsWith("/uploads/")) {
            Path path = Paths.get(filePath.substring(1)); // Remove leading slash
            Files.deleteIfExists(path);
        }
    }
}