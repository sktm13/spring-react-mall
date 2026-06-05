package org.yujin.mallapi.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yujin.mallapi.util.CustomJWTException;
import org.yujin.mallapi.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class APIRefreshController {

    // 토큰 재발급
    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new CustomJWTException("NULL_REFRESH");
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomJWTException("INVALID_ACCESS_TOKEN");
        }

        String accessToken = authHeader.substring(7);

        if (accessToken.isBlank() || accessToken.equals("undefined") || accessToken.equals("null")) {
            throw new CustomJWTException("INVALID_ACCESS_TOKEN");
        }

        // Access 토큰이 아직 만료되지 않았으면 기존 토큰 그대로 반환
        if (!checkExpiredToken(accessToken)) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        // Refresh 토큰 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);

        log.info("refresh ... claims: {}", claims);

        String newAccessToken = JWTUtil.generateToken(claims, 10);

        Object expObj = claims.get("exp");
        Integer exp = expObj instanceof Integer ? (Integer) expObj : ((Number) expObj).intValue();

        String newRefreshToken = checkTime(exp)
                ? JWTUtil.generateToken(claims, 60 * 24)
                : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    private boolean checkTime(Integer exp) {
        java.util.Date expDate = new java.util.Date((long) exp * 1000);

        long gap = expDate.getTime() - System.currentTimeMillis();

        long leftMin = gap / (1000 * 60);

        return leftMin < 60;
    }

    private boolean checkExpiredToken(String token) {
        try {
            JWTUtil.validateToken(token);
        } catch (CustomJWTException ex) {
            if (ex.getMessage().equals("Expired")) {
                return true;
            }

            throw ex;
        }

        return false;
    }
}