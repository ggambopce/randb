package com.jinho.randb.domain.opinion.application;

import com.jinho.randb.domain.opinion.dao.OpinionRepository;
import com.jinho.randb.domain.opinion.domain.Opinion;
import com.jinho.randb.domain.opinion.dto.AddOpinionRequest;
import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class OpinionServiceImpl implements OpinionService {

    private final OpinionRepository opinionRepository;
    private final PostRepository postRepository;

    @Override
    public void save(AddOpinionRequest addOpinionRequest) {
        Long postId = addOpinionRequest.getPostId();

        Optional<Post>postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 postId에 대한 Post가 존재하지 않습니다.");
        }

        Post post = postOptional.get();
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0).withSecond(0);

        Opinion opinion = Opinion.builder()
                .opinionContent(addOpinionRequest.getOpinionContent())
                .opinionType(addOpinionRequest.getOpinionType())
                .post(post)
                .created_at(localDateTime)
                .build();
        opinionRepository.save(opinion);
    }

    @Override
    public Optional<Opinion> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Opinion> findAll() {
        return null;
    }

    @Override
    public void deleteOpinion(Long OpinionId) {

    }

    @Override
    public void updateOpinion(Long opinionId, String opinionContent) {

    }
}
