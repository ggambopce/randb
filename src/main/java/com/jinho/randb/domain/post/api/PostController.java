package com.jinho.randb.domain.post.api;

import com.jinho.randb.domain.post.application.PostService;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.domain.PostStatistics;
import com.jinho.randb.domain.post.dto.user.*;
import com.jinho.randb.domain.post.exception.PostException;
import com.jinho.randb.global.exception.ErrorResponse;
import com.jinho.randb.global.exception.ex.BadRequestException;
import com.jinho.randb.global.payload.ControllerApiResponse;
import com.jinho.randb.global.security.oauth2.details.PrincipalDetails;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
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
    @PostMapping(value = "/api/user/posts")
    public ResponseEntity<?> postAdd(@Valid @RequestBody UserAddRequest userAddPostDto, BindingResult bindingResult){

        try{
            // 요청 유효성 검사
            ResponseEntity<ErrorResponse<Map<String, String>>> errorMap = getErrorResponseResponseEntity(bindingResult);
            if (errorMap != null) return errorMap;

            // 게시글 저장
            postService.save(userAddPostDto);

            return ResponseEntity.ok(new ControllerApiResponse(true, "작성 성공"));
        } catch (NoSuchElementException e) {
            throw new PostException(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorException("서버오류", e);
        }

    }

    @Operation(summary = "전체 토론글 조회 API", description = "토론글의 전체 목록을 조회할 수 있습니다.(페이지네이션)", tags = {"일반 사용자 토론글 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Post.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\" : \"조회 성공\",\"posts\":[{\"id\":23, \"postTitle\" : \"새로운 토론 주제\",\"postContent\" : \"이것은 토론의 내용입니다.\"}]}")))
    })
    @GetMapping("/api/posts")
    public ResponseEntity<?> findAllPost(@RequestParam(value = "postId", required = false) Long postId, Pageable pageable) {
        PostResponse postResponse = postService.postPage(postId, pageable);
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "조회성공", postResponse));
    }

    @Operation(summary = "메인페이지 전체 토론글 조회 API", description = "토론글의 전체 목록을 조회할 수 있습니다.", tags = {"일반 사용자 토론글 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Post.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\" : \"조회 성공\",\"posts\":[{\"id\":23, \"postTitle\" : \"새로운 토론 주제\",\"postContent\" : \"이것은 토론의 내용입니다.\"}]}")))
    })
    @GetMapping("/api/main/posts")
    public ResponseEntity<?> mainPagePost() {
        MainPagePostResponse mainPagePostResponse = postService.mainPagePost();
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "조회성공", mainPagePostResponse));
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
    @GetMapping("/api/user/detail/posts/{post-id}")
    public ResponseEntity<?> getDetail(@PathVariable("post-id") Long postId) {
        PostDetailResponse postDetailResponse = postService.getPostDetail(postId);

        // JSON 형식의 응답 반환
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "조회성공", postDetailResponse));
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
    public ResponseEntity<?> deletePost(@PathVariable("post-id") Long postId) {
        try {
            // 서비스 계층 호출
            postService.delete(postId);

            // 성공 응답 반환
            return ResponseEntity.ok(new ControllerApiResponse<>(true, "게시글 삭제 성공"));
        } catch (NoSuchElementException e) {
            throw new BadRequestException(e.getMessage());  //예외처리-> 여기서 처리안하고  @ExceptionHandler로 예외처리함
        }
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
    public ResponseEntity<?> updatePost(@Valid @RequestBody UserUpdateRequest updatePostDto, BindingResult bindingResult, @PathVariable("post-id") Long postId){

        try {
            // 유효성 검사
            ResponseEntity<ErrorResponse<Map<String, String>>> errorMap = getErrorResponseResponseEntity(bindingResult);
            if (errorMap != null) return errorMap;

            // 서비스 호출: 게시글 수정
            postService.update(postId, updatePostDto);

            // 성공 응답 반환
            return ResponseEntity.ok(new ControllerApiResponse(true, "토론글 수정 성공"));
        }catch (NoSuchElementException e){
            throw new BadRequestException(e.getMessage());  // 게시글을 찾을 수 없는 경우
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException("서버 오류 발생", e);
        }

    }

    @PostMapping("/api/user/posts/{postId}/complete")
    public ResponseEntity<?> completePost(@PathVariable("postId") Long postId) {
        try {
            PostStatistics statistics = postService.completePost(postId);
            return ResponseEntity.ok(new ControllerApiResponse<>(true, "토론이 완료되었습니다.", statistics));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ControllerApiResponse<>(false, e.getMessage()));
        }
    }




    /*
    BindingResult 의 예외 Valid 여러곳의 사용되어서 메소드로 추출
     */
    public static ResponseEntity<ErrorResponse<Map<String, String>>> getErrorResponseResponseEntity(BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            Map<String,String> errorMap = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errorMap.put(error.getField(),error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ErrorResponse<>(false, "모든 값을 입력해 주세요", errorMap));
        }
        return null;
    }


}
