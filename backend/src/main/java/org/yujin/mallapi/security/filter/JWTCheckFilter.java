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

    // generate override shouldnotfilter
    // return false = 체크함
    // return true = 체크하지 않음
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        // true == not checking
        String path = request.getRequestURI();

        log.info("check uri1------[{}]", path);
        log.info("check uri2------[{}]", path == null ? "NULL" : path);
        log.info("dispatcher------[{}]", request.getDispatcherType());

        // 로그인은 체크하지 않음
        if (path.startsWith("/api/member/")) {
            return true;
        }

        // 이미지 조회 경로는 체크하지 않음
        if (path.startsWith("/api/products/view/")) {
            return true;
        }

        // Preflight요청은 체크하지 않음
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        //ELB Health Check
        if (path.equals("/health")) {
            return true;
        }

        // false == check
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("----------------");
        log.info("----------------");
        log.info(">>> {} {}", request.getMethod(), request.getRequestURI());
        log.info("----------------");

        String authHeaderStr = request.getHeader("Authorization");

        // Bearer // 7 JWT
        try {
            // Bearer accestoken...
            String accessToken = authHeaderStr.substring(7);
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("JWT claims: " + claims);

            // filterChain.doFilter(request, response);

            // PreAuthorize로 대체
            String email = (String) claims.get("email");
            String pw = (String) claims.get("pw");
            String nickname = (String) claims.get("nickname");
            Boolean social = (Boolean) claims.get("social");
            List<String> roleNames = (List<String>) claims.get("roleNames");

            MemberDTO memberDTO = new MemberDTO(email, pw, nickname, social.booleanValue(),
                    roleNames);

            log.info("-----------------------------------");
            log.info(memberDTO);
            log.info(memberDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDTO,
                    pw, memberDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {

            log.error("JWT Check Error..............");
            log.error(e.getMessage());

            Gson gson = new Gson();

            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));
            response.setContentType("application/json");

            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();
            return;

        }

    }

}
