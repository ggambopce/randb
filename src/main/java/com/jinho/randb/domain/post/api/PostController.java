package com.jinho.randb.domain.post.api;

import com.jinho.randb.domain.post.application.PostService;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.dto.PostDto;
import com.jinho.randb.domain.post.dto.user.PostDetailResponse;
import com.jinho.randb.domain.post.dto.user.UserAddRequest;
import com.jinho.randb.domain.post.dto.user.UserUpdateRequest;
import com.jinho.randb.global.payload.ControllerApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Slf4j
public class PostController {

    private final PostService postService;

    /*
    토론게시글 등록
     */
    @PostMapping(value = "api/user/posts")
    public ResponseEntity<?> postAdd(@Valid @RequestBody UserAddRequest userAddPostDto){

        postService.save(userAddPostDto);

        return ResponseEntity.ok(new ControllerApiResponse(true, "작성 성공"));
    }

    /*
    토론게시글 목록조회
     */
    @GetMapping("/api/posts")
    public ResponseEntity<?> findAllPost() {
        List<Post> posts = postService.findAll();
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "조회성공", posts));
    }

    /*
    토론게시글 상세조회
     */
    @GetMapping("/api/user/posts/{post-id}")
    public ResponseEntity<?> findPost(@PathVariable("post-id") long id) {
        Optional<Post> post = postService.findById(id);
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "조회성공", post));
    }
    /*
    토론게시글 삭제
     */
    @DeleteMapping("/api/user/posts/{post-id}")
    public ResponseEntity<?> deletePost(@PathVariable("post-id") Long postId){
        postService.delete(postId);
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "게시글 삭제 성공"));
    }

    /*
    토론게시글 수정
    */
    @PostMapping("/api/user/update/posts/{post-id}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody UserUpdateRequest updatePostDto, @PathVariable("post-id") Long postId){
        postService.update(postId, updatePostDto);

        return ResponseEntity.ok(new ControllerApiResponse(true,"토론글 수정 성공"));

    }

}
