package org.yujin.mallapi.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yujin.mallapi.dto.MemberDTO;
import org.yujin.mallapi.dto.MemberModifyDTO;
import org.yujin.mallapi.service.MemberService;
import org.yujin.mallapi.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class SocialController {

    private final MemberService memberService;

    @GetMapping("/api/member/kakao")
    public Map<String, Object> getMemberFromKakao(
            @RequestParam(name = "code") String code) {

        log.info("kakao authorization code: {}", code);

        MemberDTO memberDTO = memberService.getKakaoMemberByCode(code);

        Map<String, Object> claims = memberDTO.getClaims();

        String jwtAccessToken = JWTUtil.generateToken(claims, 10);
        String jwtRefreshToken = JWTUtil.generateToken(claims, 60 * 24);

        claims.put("accessToken", jwtAccessToken);
        claims.put("refreshToken", jwtRefreshToken);

        return claims;
    }

    @PutMapping("/api/member/modify")
    public Map<String, String> modify(@RequestBody MemberModifyDTO memberModifyDTO) {

        log.info("member modify: {}", memberModifyDTO);

        memberService.modifyMember(memberModifyDTO);

        return Map.of("result", "modified");
    }
}