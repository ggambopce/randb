package com.jinho.randb.domain.opinion.dao;

import com.jinho.randb.domain.opinion.domain.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Long> {
    List<Opinion> findByPostId(Long postId);
}
