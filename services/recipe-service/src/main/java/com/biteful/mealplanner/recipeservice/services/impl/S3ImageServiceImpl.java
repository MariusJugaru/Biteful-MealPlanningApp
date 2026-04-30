package com.biteful.mealplanner.recipeservice.services.impl;

import com.biteful.mealplanner.recipeservice.services.FileService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@Profile("s3")
public class S3ImageServiceImpl implements FileService {

    private final S3Client s3Client;
    private final String bucket = "biteful-images";

    public S3ImageServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String save(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request,
                    RequestBody.fromBytes(file.getBytes()));
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload to S3", e);
        }
    }

    @Override
    public void delete(String imageUrl) {
        if (imageUrl == null) return;

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(imageUrl)
                .build());
    }
}
