package com.biteful.mealplanner.recipeservice.services.impl;

import com.biteful.mealplanner.recipeservice.services.FileService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Profile("default")
public class LocalImageServiceImpl implements FileService {

    private final String UPLOAD_DIR = "uploads/";

    @Override
    public String save(MultipartFile file) {
        if (file == null || file.isEmpty())
            return null;

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path dir = Paths.get(UPLOAD_DIR);
            Files.createDirectories(dir);

            Path path = dir.resolve(fileName);

            Files.write(path, file.getBytes());

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

    @Override
    public void delete(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) return;

        try {
            String filename = Paths.get(imageUrl).getFileName().toString();

            Path path = Paths.get(UPLOAD_DIR).resolve(filename);

            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }

}
