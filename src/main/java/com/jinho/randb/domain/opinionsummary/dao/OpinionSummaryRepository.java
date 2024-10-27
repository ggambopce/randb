package com.jinho.randb.domain.opinionsummary.dao;

import com.jinho.randb.domain.opinionsummary.domain.OpinionSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpinionSummaryRepository extends JpaRepository<OpinionSummary, Long> {
}
