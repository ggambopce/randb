package com.jinho.randb.domain.account.api.user;

import com.jinho.randb.domain.account.application.user.SignUpService;
import com.jinho.randb.domain.account.dto.request.JoinRequest;
import com.jinho.randb.global.exception.ErrorResponse;
import com.jinho.randb.global.payload.ControllerApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name="일반 - 회원가입 컨트롤러", description = "이메일 회원가입 및 검증 처리")
@RequestMapping("/api")
public class SignUpValidController {

    private final SignUpService signUpService;

    @Operation(summary = "회원가입", description = "사용자가 회원가입합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\": \"회원가입 성공\"}"))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\":false,\"message\":\"실패\",\"data\":{\"필드명\" : \"필드 오류 내용\"}}"))),
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody JoinRequest joinRequest, BindingResult bindingResult) {

        // 유효성 검증 실패 시 400 Bad Request 반환
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(new ControllerApiResponse<>(false, "입력값오류", errors));
        }

        // 유효성 검증 통과 시 서비스 로직 실행
        signUpService.joinAccount(JoinRequest.fromDto(joinRequest));

        return ResponseEntity.ok(new ControllerApiResponse<>(true,"회원가입 성공"));
    }

    private static ResponseEntity<ErrorResponse<Map<String, String>>> getErrorResponseResponse(BindingResult bindingResult,Map<String, String> map) {
        Map<String, String> result = new LinkedHashMap<>();

        for(Map.Entry<String,String> entry : map.entrySet()){
            result.put(entry.getKey(),entry.getValue());
        }
        for (FieldError error : bindingResult.getFieldErrors()) {
            result.put(error.getField(),error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(new ErrorResponse<>(false, "실패", result));
    }

}
