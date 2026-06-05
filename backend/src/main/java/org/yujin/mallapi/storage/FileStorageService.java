package org.yujin.mallapi.storage;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    List<String> saveFiles(List<MultipartFile> files);

    ResponseEntity<Resource> getFile(String fileName);

    void deleteFiles(List<String> fileNames);
}