package com.jinho.randb.domain.post.api;

import com.jinho.randb.domain.post.application.PostService;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.dto.user.UserAddRequest;
import com.jinho.randb.domain.post.dto.user.UserUpdateRequest;
import com.jinho.randb.global.payload.ControllerApiResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@OpenAPIDefinition(tags = {
        @Tag(name = "일반 사용자 토론글 컨트롤러", description = "일반 사용자 토론글 관련 작업")
})
@Slf4j
public class PostController {

    private final PostService postService;

    @Operation(summary = "토론글 작성 API", description = "토론글 작성", tags = {"일반 사용자 토론글 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                    examples = @ExampleObject(value = "{\"success\": true, \"message\" : \"작성 성공\"}"))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\": false, \"message\" : \"모든 값을 입력해 주세요\"}")))
    })
    @PostMapping(value = "api/user/posts")
    public ResponseEntity<?> postAdd(@Valid @RequestBody UserAddRequest userAddPostDto){

        postService.save(userAddPostDto);

        return ResponseEntity.ok(new ControllerApiResponse(true, "작성 성공"));
    }

    @Operation(summary = "전체 토론글 조회 API", description = "토론글의 전체 목록을 조회할 수 있습니다.", tags = {"일반 사용자 토론글 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Post.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\" : \"조회 성공\",\"posts\":[{\"id\":23, \"postTitle\" : \"새로운 토론 주제\",\"postContent\" : \"이것은 토론의 내용입니다.\"}]}")))
    })
    @GetMapping("/api/posts")
    public ResponseEntity<?> findAllPost() {
        List<Post> posts = postService.findAll();
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "조회성공", posts));
    }

    @Operation(summary = "토론글 상세 조회 API", description = "토론글의 상세 정보를 조회할 수 있습니다.", tags = {"일반 사용자 토론글 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Post.class),
                            examples = @ExampleObject(value = "{\"success\":true,\"message\":\"조회성공\",\"data\":{\"post\":{\"id\":3,\"postTitle\":\"토론 주제\",\"postContent\":\"토론 내용입니다!\"}}}"))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples =  @ExampleObject(value = "{\"success\": false, \"message\": \"해당하는 게시물이 없습니다.\"}")))
    })
    @GetMapping("/api/user/posts/{post-id}")
    public ResponseEntity<?> findPost(@PathVariable("post-id") long id) {
        Optional<Post> post = postService.findById(id);
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "조회성공", post));
    }

    @Operation(summary = "토론글 삭제 API",description = "삭제시 해당 게시물과 관련된 데이터는 모두 삭제",tags = {"일반 사용자 토론글 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\" : \"토론글 삭제 성공\"}"))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\": false, \"message\" : \"게시글을 찾을수 없습니다.\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\": false, \"message\" : \"작성자만 삭제할수 있습니다.\"}")))
    })
    @DeleteMapping("/api/user/posts/{post-id}")
    public ResponseEntity<?> deletePost(@PathVariable("post-id") Long postId){
        postService.delete(postId);
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "게시글 삭제 성공"));
    }

    @Operation(summary = "토론글 수정 API", description = "로그인한 사용자만 수정이 가능하며 작성자만 수정이 가능하도록 이전에 비밀번호 검증을 통해서 검증확인해 해당 API 접근가능", tags = {"일반 사용자 토론글 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\": \"토론글 수정 성공\"}"))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\": false, \"message\" : \"게시글을 찾을수 없습니다.\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\": false, \"message\" : \"작성자만 수정할 수 있습니다.\"}")))
    })
    @PostMapping("/api/user/update/posts/{post-id}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody UserUpdateRequest updatePostDto, @PathVariable("post-id") Long postId){
        postService.update(postId, updatePostDto);

        return ResponseEntity.ok(new ControllerApiResponse(true,"토론글 수정 성공"));

    }

}
