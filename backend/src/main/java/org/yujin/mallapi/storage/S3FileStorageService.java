package org.yujin.mallapi.storage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Log4j2
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.storage.type", havingValue = "s3")
public class S3FileStorageService implements FileStorageService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public List<String> saveFiles(List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            return List.of();
        }

        List<String> uploadedNames = new ArrayList<>();

        for (MultipartFile file : files) {

            if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
                continue;
            }

            try {
                String savedName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                PutObjectRequest originalRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(savedName)
                        .contentType(file.getContentType())
                        .build();

                s3Client.putObject(
                        originalRequest,
                        RequestBody.fromBytes(file.getBytes())
                );

                String contentType = file.getContentType();

                if (contentType != null && contentType.startsWith("image")) {

                    ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();

                    Thumbnails.of(file.getInputStream())
                            .size(200, 200)
                            .toOutputStream(thumbnailOutputStream);

                    PutObjectRequest thumbRequest = PutObjectRequest.builder()
                            .bucket(bucket)
                            .key("s_" + savedName)
                            .contentType(contentType)
                            .build();

                    s3Client.putObject(
                            thumbRequest,
                            RequestBody.fromBytes(thumbnailOutputStream.toByteArray())
                    );
                }

                uploadedNames.add(savedName);

            } catch (Exception e) {
                throw new RuntimeException("S3 file upload failed", e);
            }
        }

        return uploadedNames;
    }

    @Override
    public ResponseEntity<Resource> getFile(String fileName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes =
                    s3Client.getObjectAsBytes(getObjectRequest);

            ByteArrayResource resource = new ByteArrayResource(objectBytes.asByteArray());

            return ResponseEntity.ok()
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("S3 file read failed: " + fileName, e);
        }
    }

    @Override
    public void deleteFiles(List<String> fileNames) {

        if (fileNames == null || fileNames.isEmpty()) {
            return;
        }

        for (String fileName : fileNames) {

            try {
                s3Client.deleteObject(
                        DeleteObjectRequest.builder()
                                .bucket(bucket)
                                .key(fileName)
                                .build()
                );

                s3Client.deleteObject(
                        DeleteObjectRequest.builder()
                                .bucket(bucket)
                                .key("s_" + fileName)
                                .build()
                );
            } catch (Exception e) {
                log.warn("S3 file delete failed: {}", fileName, e);
            }
        }
    }
}