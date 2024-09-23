package com.jinho.randb.domain.member.application;

import com.jinho.randb.domain.member.domain.Member;
import com.jinho.randb.domain.member.dto.MemberDto;

public interface MemberService {

    Member saveEntity(Member member);

    void saveDto(MemberDto memberDto);

    MemberDto findByLoginId(String loginId);

    void deleteMember(Long memberId);
}
