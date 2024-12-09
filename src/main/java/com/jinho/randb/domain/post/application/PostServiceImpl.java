package com.jinho.randb.domain.post.application;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.account.dto.AccountContext;
import com.jinho.randb.domain.account.dto.AccountDto;
import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.domain.PostType;
import com.jinho.randb.domain.post.dto.PostDto;
import com.jinho.randb.domain.post.dto.user.*;
import com.jinho.randb.global.security.oauth2.details.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        // SecurityContextHolder에서 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Principal 객체를 안전하게 캐스팅
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof PrincipalDetails principalDetails)) {
            throw new IllegalStateException("인증된 사용자 정보가 PrincipalDetails 타입이 아닙니다.");
        }

        // PrincipalDetails에서 AccountDto 가져오기
        AccountDto accountDto = principalDetails.getAccountDto();
        Long accountId = accountDto.getId();

        // Account 조회
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("계정을 찾을 수 없습니다."));

        // DTO -> domain 변환
        Post post = Post.builder()
                .postTitle(userAddRequest.getPostTitle())
                .postContent(userAddRequest.getPostContent())
                .account(account) // 작성자 정보 설정
                .type(PostType.DISCUSSING) // 기본상태를 DISCUSSING으로 설정
                .createdAt(LocalDateTime.now())
                .build();

        postRepository.save(post);
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
    public void delete(Long postId) {
    // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof PrincipalDetails)) {
            throw new AccessDeniedException("로그인된 사용자만 접근 가능합니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetails.getUsername();

        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다."));

        // 작성자 확인
        if (!post.getAccount().getUsername().equals(username)) {
            throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
        }

        // 게시글 삭제
        postRepository.delete(post);
    }

    @Override
    public void update(Long postId, UserUpdateRequest userUpdatePostDto) {

        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다."));

        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof PrincipalDetails)) {
            throw new AccessDeniedException("로그인된 사용자만 접근 가능합니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetails.getUsername();

        // 작성자 검증
        if (!post.getAccount().getUsername().equals(username)) {
            throw new AccessDeniedException("작성자만 수정 가능합니다.");
        }

        // 게시글 수정
        post.update(userUpdatePostDto.getPostTitle(), userUpdatePostDto.getPostContent());

        // 변경 사항 저장
        postRepository.save(post);
    }

    @Override
    public void updatePostType(Long postId, PostType newType) {
        // Post 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글을 찾을 수 없습니다."));

        // 상태 변경
        post.updatePostType(newType);
        postRepository.save(post); // 변경사항 저장
    }

}
