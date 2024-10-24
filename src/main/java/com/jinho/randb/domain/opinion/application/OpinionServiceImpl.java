package com.jinho.randb.domain.opinion.application;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.account.dto.AccountContext;
import com.jinho.randb.domain.opinion.dao.OpinionRepository;
import com.jinho.randb.domain.opinion.domain.Opinion;
import com.jinho.randb.domain.opinion.dto.AddOpinionRequest;
import com.jinho.randb.domain.opinion.dto.OpinionContentAndTypeDto;
import com.jinho.randb.domain.opinion.dto.OpinionDto;
import com.jinho.randb.domain.opinion.dto.UserUpdateOpinionDto;
import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class OpinionServiceImpl implements OpinionService {

    private final OpinionRepository opinionRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    @Override
    public Opinion save(AddOpinionRequest addOpinionRequest) {
        // SecurityContextHolder에서 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountContext accountContext = (AccountContext) authentication.getPrincipal();
        Long accountId = accountContext.getAccountDto().getId();  // 로그인된 사용자의 accountId 가져오기

        Long postId = addOpinionRequest.getPostId();

        Optional<Account>op_account = accountRepository.findById(accountId);
        Optional<Post>op_post = postRepository.findById(postId);

        if (op_account.isPresent() && op_post.isPresent()) {     //사용자 정보와 게시글의 정보가 존재할시에만 통과
            Account account = op_account.get();
            Post post = op_post.get();
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0).withSecond(0);

            Opinion opinion = Opinion.builder()     //의견 저장
                    .opinionContent(addOpinionRequest.getOpinionContent())
                    .opinionType(addOpinionRequest.getOpinionType())
                    .account(account)
                    .post(post)
                    .created_at(localDateTime)
                    .build();
            return opinionRepository.save(opinion);
        }else {
            throw new NoSuchElementException("회원정보나 게시글을 찾을수 없습니다.");   //사용자 및 게시글이 없을시에는 해당 예외발생
        }
    }

    @Override
    public Optional<Opinion> findById(Long id) {
        return opinionRepository.findById(id);
    }

    @Override
    public List<OpinionContentAndTypeDto> findByPostId(Long postId) {
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
