package com.jinho.randb.domain.post.application;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.dto.PostDto;
import com.jinho.randb.domain.post.dto.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    @Override
    public void save(UserAddRequest userAddRequest) {
        Long accountId = userAddRequest.getAccountId();

        Optional<Account> op_account = accountRepository.findById(accountId);

        if (op_account.isPresent()) {

            Account account = op_account.get();
            // DTO -> domain 변환
            Post post = Post.builder()
                    .postTitle(userAddRequest.getPostTitle())
                    .postContent(userAddRequest.getPostContent())
                    .account(account)
                    .createdAt(LocalDateTime.now())
                    .build();

            postRepository.save(post);

        } else {

            throw new NoSuchElementException("토론글 저장에 실패했습니다.");

        }

    }
    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    /**
     * 레시피의 상세정보를 보는 로직,
     * @param postId  찾을 토론글 번호
     * @return      Response로 변환해 해당 토론글의 상세 정보를 반환
     */
    @Override
    public PostDetailResponse getPostDetail(Long postId) {
        PostDto postDetail = postRepository.getPostDetail(postId);
        return PostDetailResponse.of(postDetail.toDto());
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public PostResponse postPage(Long postId, Pageable pageable) {

        Slice<PostDto> allPost = postRepository.getAllPost(postId, pageable);

        return new PostResponse(allPost.hasNext(),allPost.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public MainPagePostResponse mainPagePost() {
        List<PostDto> postDtoList = postRepository.mainPagePost();
        return MainPagePostResponse.of(postDtoList);
    }


    @Override
    public void delete(String username, Long postId) {

        Account account = accountRepository.findByUsername(username);
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을수 없습니다."));
        if(!post.getAccount().getUsername().equals(account.getUsername())) throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");

        postRepository.deleteAccountId(account.getId(), postId);
    }

    @Override
    public void update(Long postId, UserUpdateRequest userUpdatePostDto, String username) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다."));
        if(!post.getAccount().getUsername().equals(username)) throw new AccessDeniedException("작성자만 수정 가능합니다.");

        post.update(userUpdatePostDto.getPostTitle(), userUpdatePostDto.getPostContent());

        postRepository.save(post);

    }


}
