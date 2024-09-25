package com.jinho.randb.domain.post.api;

import com.jinho.randb.domain.post.application.PostService;
import com.jinho.randb.domain.post.dto.user.UserAddRequest;
import com.jinho.randb.global.payload.ControllerApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping(value = "api/user/posts")
    public ResponseEntity<?> postAdd(@Valid @RequestPart UserAddRequest userAddPostDto){

        postService.save(userAddPostDto);

        return ResponseEntity.ok(new ControllerApiResponse(true, "작성 성공"));
    }
}
