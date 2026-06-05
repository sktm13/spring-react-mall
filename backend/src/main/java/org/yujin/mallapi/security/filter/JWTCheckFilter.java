package org.yujin.mallapi.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yujin.mallapi.dto.MemberDTO;
import org.yujin.mallapi.util.JWTUtil;

import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();

        // Preflight 요청 제외
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        // Health Check 제외
        if (path.equals("/health")) {
            return true;
        }

        // 로그인 제외
        if (path.equals("/api/member/login")) {
            return true;
        }

        // 카카오 로그인 제외
        if (path.equals("/api/member/kakao")) {
            return true;
        }

        // 토큰 재발급 제외
        if (path.equals("/api/member/refresh")) {
            return true;
        }

        // 이미지 조회 제외
        if (path.startsWith("/api/products/view/")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeaderStr = request.getHeader("Authorization");

        if (authHeaderStr == null || !authHeaderStr.startsWith("Bearer ")) {
            sendJWTError(response);
            return;
        }

        String accessToken = authHeaderStr.substring(7);

        try {
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            String email = (String) claims.get("email");
            String pw = (String) claims.get("pw");
            String nickname = (String) claims.get("nickname");
            Boolean social = (Boolean) claims.get("social");
            List<String> roleNames = (List<String>) claims.get("roleNames");

            MemberDTO memberDTO = new MemberDTO(
                    email,
                    pw,
                    nickname,
                    social != null && social,
                    roleNames
            );

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            memberDTO,
                            pw,
                            memberDTO.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception e) {
            log.warn("JWT validation failed: {}", e.getMessage());

            sendJWTError(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendJWTError(HttpServletResponse response) throws IOException {

        Gson gson = new Gson();

        String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter printWriter = response.getWriter();
        printWriter.println(msg);
        printWriter.close();
    }
}