package com.jinho.randb.domain.opinion.application;

import com.jinho.randb.domain.opinion.dao.OpinionRepository;
import com.jinho.randb.domain.opinion.domain.Opinion;
import com.jinho.randb.domain.opinion.dto.AddOpinionRequest;
import com.jinho.randb.domain.opinion.dto.UserUpdateOpinionDto;
import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
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
        return opinionRepository.findById(id);
    }

    @Override
    public List<Opinion> findByPostId(Long postId) {
        return opinionRepository.findByPostId(postId);
    }

    @Override
    public void delete(Long opinionId) {

        Opinion opinion = opinionRepository.findById(opinionId).orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을수 없습니다."));
        opinionRepository.deleteById(opinion.getId());

    }

    @Override
    public void update(Long opinionId, UserUpdateOpinionDto userUpdateOpinionDto) {
        Opinion opinion = opinionRepository.findById(opinionId).orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을수 없습니다."));

        opinion.update(userUpdateOpinionDto.getOpinionContent());

        opinionRepository.save(opinion);

    }
}
