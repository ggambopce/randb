package com.jinho.randb.domain.member.application;

import com.jinho.randb.domain.member.dao.MemberRepository;
import com.jinho.randb.domain.member.domain.Member;
import com.jinho.randb.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;


    @Override
    public Member saveEntity(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public void saveDto(MemberDto memberDto) {
    }

    @Override
    public MemberDto findByLoginId(String loginId) {
        return null;
    }

    @Override
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
