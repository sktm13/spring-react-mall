package org.yujin.mallapi.service;

import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.yujin.mallapi.domain.Member;
import org.yujin.mallapi.dto.MemberDTO;
import org.yujin.mallapi.dto.MemberModifyDTO;

@Transactional
public interface MemberService {

    // 기존 방식: 카카오 accessToken으로 회원 조회
    MemberDTO getKakaoMember(String accessToken);

    // 추가: 카카오 authorization code로 회원 조회
    MemberDTO getKakaoMemberByCode(String code);

    void modifyMember(MemberModifyDTO memberModifyDTO);
    
    default MemberDTO entityToDTO(Member member) {

        MemberDTO dto = new MemberDTO(
                member.getEmail(), 
                member.getPw(), 
                member.getNickname(), 
                member.isSocial(), 
                member.getMemberRoleList().stream()
                        .map(memberRole -> memberRole.name())
                        .collect(Collectors.toList())
        );

        return dto;
    }
}