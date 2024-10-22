package com.jinho.randb.domain.opinionsummary.application;

import com.jinho.randb.domain.opinionsummary.domain.OpinionSummary;

import java.util.Map;

public interface OpinionSummaryService {

    Map<String, String> saveOpinionSummary(Long postId);
}
