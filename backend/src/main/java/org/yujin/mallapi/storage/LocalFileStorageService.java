package org.yujin.mallapi.storage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;

@Service
@Log4j2
@ConditionalOnProperty(name = "app.storage.type", havingValue = "local", matchIfMissing = true)
public class LocalFileStorageService implements FileStorageService {

    @Value("${app.storage.local-path}")
    private String uploadPath;

    private Path uploadRootPath;

    @PostConstruct
    public void init() {
        try {
            Path path = Paths.get(uploadPath);

            // uploadPath가 상대경로라면 현재 실행 위치 기준으로 변환
            // backend 폴더에서 실행하면 backend/upload 가 됨
            if (!path.isAbsolute()) {
                path = Paths.get(System.getProperty("user.dir")).resolve(uploadPath);
            }

            uploadRootPath = path.toAbsolutePath().normalize();

            Files.createDirectories(uploadRootPath);

            log.info("user.dir: {}", System.getProperty("user.dir"));
            log.info("Local upload path: {}", uploadRootPath);

        } catch (Exception e) {
            throw new RuntimeException("Could not create upload folder", e);
        }
    }

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
                String originalName = file.getOriginalFilename();

                String safeName = originalName
                        .replace("\\", "_")
                        .replace("/", "_")
                        .replace(" ", "_");

                String savedName = UUID.randomUUID() + "_" + safeName;

                Path savePath = uploadRootPath.resolve(savedName).normalize();

                log.info("originalName: {}", originalName);
                log.info("savedName: {}", savedName);
                log.info("savePath: {}", savePath);

                Files.copy(
                        file.getInputStream(),
                        savePath,
                        StandardCopyOption.REPLACE_EXISTING
                );

                String contentType = file.getContentType();

                if (contentType != null && contentType.startsWith("image")) {
                    Path thumbPath = uploadRootPath.resolve("s_" + savedName).normalize();

                    Thumbnails.of(savePath.toFile())
                            .size(200, 200)
                            .toFile(thumbPath.toFile());
                }

                uploadedNames.add(savedName);

            } catch (Exception e) {
                log.error("Local file upload failed", e);
                throw new RuntimeException("Local file upload failed", e);
            }
        }

        return uploadedNames;
    }

    @Override
    public ResponseEntity<Resource> getFile(String fileName) {

        try {
            Path filePath = uploadRootPath.resolve(fileName).normalize();
            File file = filePath.toFile();

            if (!file.exists()) {
                File defaultFile = uploadRootPath.resolve("default.jpeg").toFile();

                if (defaultFile.exists()) {
                    Resource defaultResource = new FileSystemResource(defaultFile);
                    return ResponseEntity.ok().body(defaultResource);
                }

                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);

            return ResponseEntity.ok().body(resource);

        } catch (Exception e) {
            log.warn("Local file read failed: {}", fileName, e);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public void deleteFiles(List<String> fileNames) {

        if (fileNames == null || fileNames.isEmpty()) {
            return;
        }

        for (String fileName : fileNames) {
            try {
                Path originalPath = uploadRootPath.resolve(fileName).normalize();
                Path thumbPath = uploadRootPath.resolve("s_" + fileName).normalize();

                Files.deleteIfExists(originalPath);
                Files.deleteIfExists(thumbPath);

            } catch (Exception e) {
                log.warn("Local file delete failed: {}", fileName, e);
            }
        }
    }
}