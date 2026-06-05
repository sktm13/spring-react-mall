package org.yujin.mallapi.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.yujin.mallapi.domain.Member;
import org.yujin.mallapi.domain.MemberRole;
import org.yujin.mallapi.dto.MemberDTO;
import org.yujin.mallapi.dto.MemberModifyDTO;
import org.yujin.mallapi.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    @Value("${kakao.client-secret:}")
    private String kakaoClientSecret;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Override
    public MemberDTO getKakaoMemberByCode(String code) {

        String kakaoAccessToken = getKakaoAccessToken(code);

        return getKakaoMember(kakaoAccessToken);
    }

    @Override
    public MemberDTO getKakaoMember(String accessToken) {

        String email = getEmailFromKakaoAccessToken(accessToken);

        log.info("email: {}", email);

        Optional<Member> result = memberRepository.findById(email);

        // 기존 회원
        if (result.isPresent()) {
            return entityToDTO(result.get());
        }

        // 신규 소셜 회원
        Member socialMember = makeSocialMember(email);
        memberRepository.save(socialMember);

        return entityToDTO(socialMember);
    }

    private String getKakaoAccessToken(String code) {

        String kakaoTokenURL = "https://kauth.kakao.com/oauth/token";

        if (code == null || code.isBlank()) {
            throw new RuntimeException("Kakao authorization code is null");
        }

        if (kakaoRestApiKey == null || kakaoRestApiKey.isBlank()) {
            throw new RuntimeException("KAKAO_REST_API_KEY is empty");
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoRestApiKey);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        // 카카오 Developers에서 Client Secret 사용 설정을 켠 경우에만 사용
        if (kakaoClientSecret != null && !kakaoClientSecret.isBlank()) {
            params.add("client_secret", kakaoClientSecret);
        }

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(params, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(kakaoTokenURL, request, Map.class);

        Map<String, Object> body = response.getBody();

        if (body == null || body.get("access_token") == null) {
            throw new RuntimeException("Kakao access token response is invalid");
        }

        return body.get("access_token").toString();
    }

    private String getEmailFromKakaoAccessToken(String accessToken) {

        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        if (accessToken == null || accessToken.isBlank()) {
            throw new RuntimeException("Kakao access token is null");
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder =
                UriComponentsBuilder.fromUriString(kakaoGetUserURL).build();

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(
                        uriBuilder.toString(),
                        HttpMethod.GET,
                        entity,
                        LinkedHashMap.class
                );

        log.info("Kakao user response: {}", response);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        if (bodyMap == null) {
            throw new RuntimeException("Kakao user response body is null");
        }

        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");

        if (kakaoAccount == null || kakaoAccount.get("email") == null) {
            throw new RuntimeException("Kakao email is not provided");
        }

        log.info("kakaoAccount: {}", kakaoAccount);

        return kakaoAccount.get("email");
    }

    private String makeTempPassword() {

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < 10; i++) {
            buffer.append((char) ((int) (Math.random() * 55) + 65));
        }

        return buffer.toString();
    }

    private Member makeSocialMember(String email) {

        String tempPassword = makeTempPassword();

        log.info("tempPassword: {}", tempPassword);

        String nickname = "소셜회원";

        Member member = Member.builder()
                .email(email)
                .pw(passwordEncoder.encode(tempPassword))
                .nickname(nickname)
                .social(true)
                .build();

        member.addRole(MemberRole.USER);

        return member;
    }

    @Override
    public void modifyMember(MemberModifyDTO memberModifyDTO) {

        Optional<Member> result = memberRepository.findById(memberModifyDTO.getEmail());

        Member member = result.orElseThrow();

        member.changePw(passwordEncoder.encode(memberModifyDTO.getPw()));
        member.changeSocial(false);
        member.changeNickname(memberModifyDTO.getNickname());

        memberRepository.save(member);
    }
}