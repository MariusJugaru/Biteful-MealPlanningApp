package com.biteful.mealplanner.recipeservice.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String save(MultipartFile file);

    void delete(String imageUrl);
}
