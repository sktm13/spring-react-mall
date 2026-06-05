package org.yujin.mallapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yujin.mallapi.dto.PageRequestDTO;
import org.yujin.mallapi.dto.PageResponseDTO;
import org.yujin.mallapi.dto.ProductDTO;
import org.yujin.mallapi.service.ProductService;
import org.yujin.mallapi.storage.FileStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {

    private final FileStorageService fileStorageService;
    private final ProductService productService;

    // 파일 조회
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable(name = "fileName") String fileName) {
        return fileStorageService.getFile(fileName);
    }

    // 리스트 조회
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        return productService.getList(pageRequestDTO);
    }

    // 상품 등록
    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) {

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadedFileNames = fileStorageService.saveFiles(files);

        productDTO.setUploadFileNames(uploadedFileNames);

        log.info(uploadedFileNames);

        Long pno = productService.register(productDTO);

        return Map.of("result", pno);
    }

    // 단품 조회
    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable(name = "pno") Long pno) {
        return productService.get(pno);
    }

    // 수정
    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable(name = "pno") Long pno, ProductDTO productDTO) {

        productDTO.setPno(pno);

        // 기존 상품 정보 조회
        ProductDTO oldProductDTO = productService.get(pno);

        // 새로 업로드된 파일 저장
        List<MultipartFile> files = productDTO.getFiles();
        List<String> currentUploadFileNames = fileStorageService.saveFiles(files);

        // 화면에서 유지하기로 한 기존 파일명
        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        if (uploadedFileNames == null) {
            uploadedFileNames = new ArrayList<>();
        }

        // 새로 업로드된 파일명을 유지 목록에 추가
        if (currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        productDTO.setUploadFileNames(uploadedFileNames);

        // 상품 정보 수정
        productService.modify(productDTO);

        // 기존 파일 중 현재 유지 목록에 없는 파일 삭제
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();

        if (oldFileNames != null && !oldFileNames.isEmpty()) {

            List<String> finalUploadedFileNames = uploadedFileNames;

            List<String> removeFiles = oldFileNames.stream()
                    .filter(fileName -> !finalUploadedFileNames.contains(fileName))
                    .collect(Collectors.toList());

            fileStorageService.deleteFiles(removeFiles);
        }

        return Map.of("RESULT", "SUCCESS");
    }

    // 삭제
    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable(name = "pno") Long pno) {

        List<String> oldFileNames = productService.get(pno).getUploadFileNames();

        productService.remove(pno);

        fileStorageService.deleteFiles(oldFileNames);

        return Map.of("RESULT", "SUCCESS");
    }
}