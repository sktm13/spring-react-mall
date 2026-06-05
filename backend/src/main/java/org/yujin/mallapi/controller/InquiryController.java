package org.yujin.mallapi.controller;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yujin.mallapi.dto.InquiryDTO;
import org.yujin.mallapi.dto.PageRequestDTO;
import org.yujin.mallapi.dto.PageResponseDTO;
import org.yujin.mallapi.service.InquiryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    // 조회
    @GetMapping("/{ino}")
    public InquiryDTO get(@PathVariable("ino") Long ino) {
        return inquiryService.get(ino);
    }

    // 리스트 (페이징 + 검색)
    @GetMapping("/list")
    public PageResponseDTO<InquiryDTO> list(
            @RequestParam(name = "keyword", required = false) String keyword,
            PageRequestDTO pageRequestDTO) {

        log.info("keyword: {}", keyword);
        log.info("pageRequestDTO: {}", pageRequestDTO);

        return inquiryService.getList(keyword, pageRequestDTO);
    }

    // 등록
    @PostMapping("/")
    public Map<String, Long> register(@RequestBody InquiryDTO inquiryDTO) {

        log.info("InquiryDTO: {}", inquiryDTO);

        Long ino = inquiryService.register(inquiryDTO);

        return Map.of("INO", ino);
    }

    // 수정
    @PutMapping("/{ino}")
    public Map<String, String> modify(
            @PathVariable("ino") Long ino,
            @RequestBody InquiryDTO inquiryDTO) {

        inquiryDTO.setIno(ino);

        log.info("Modify: {}", inquiryDTO);

        inquiryService.modify(inquiryDTO);

        return Map.of("RESULT", "SUCCESS");
    }

    // 삭제
    @DeleteMapping("/{ino}")
    public Map<String, String> remove(@PathVariable("ino") Long ino) {

        log.info("Remove: {}", ino);

        inquiryService.remove(ino);

        return Map.of("RESULT", "SUCCESS");
    }

    // 관리자 답변
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reply/{ino}")
    public Map<String, String> reply(
            @PathVariable("ino") Long ino,
            @RequestBody Map<String, String> body) {

        String reply = body.get("reply");

        inquiryService.reply(ino, reply);

        return Map.of("RESULT", "SUCCESS");
    }
}