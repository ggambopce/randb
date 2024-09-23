package com.jinho.randb.domain.member.dao;

import com.jinho.randb.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>, CustomMemberRepository {

    Member findByLoginId(String loginId);

    List<Member> findByEmail(String email);

    List<Member> findByUsernameAndEmail(String username,String email);

    void deleteById(Long memberId);

    long countAllBy();
}
