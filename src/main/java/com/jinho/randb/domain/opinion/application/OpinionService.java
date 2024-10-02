package com.jinho.randb.domain.opinion.application;

import com.jinho.randb.domain.opinion.domain.Opinion;
import com.jinho.randb.domain.opinion.dto.AddOpinionRequest;

import java.util.List;
import java.util.Optional;

public interface OpinionService {

    void save(AddOpinionRequest addOpinionRequest);

    Optional<Opinion> findById(Long id);

    List<Opinion> findAll();

    void deleteOpinion(Long OpinionId);

    void updateOpinion(Long opinionId, String opinionContent);
}
