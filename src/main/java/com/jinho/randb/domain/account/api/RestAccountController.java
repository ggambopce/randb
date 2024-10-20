package com.jinho.randb.domain.account.api;

import com.jinho.randb.domain.account.application.AccountService;
import com.jinho.randb.domain.account.dto.AccountDto;
import com.jinho.randb.global.exception.ErrorResponse;
import com.jinho.randb.global.exception.ex.BadRequestException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "회원가입 컨트롤러",description = "회원가입을 하기위한 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestAccountController {

    private final AccountService accountService;

    @Operation(summary = "회원가입", description = "사용자가 회원가입합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\": \"회원가입 성공\"}"))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "[{\"success\":false,\"message\":\"실패\",\"data\":{\"[필드명]\":\"[필드 오류 내용]\", \"globalError\": \"모든 검사를 검증해주세요\"}} , {\"success\":false,\"message\":\"인증번호가 일치하지 않습니다.\"}]"))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody AccountDto accountDto, BindingResult result){

        try {

            if (result.hasErrors()) {

                Map<String,String> errorMessage=new HashMap<>();
                for (FieldError error : result.getFieldErrors()) {
                    errorMessage.put(error.getField(),error.getDefaultMessage());
                }

                ObjectError globalError = result.getGlobalError();

                errorMessage.put(globalError.getObjectName(), globalError.getDefaultMessage());
                ErrorResponse<Object> errorResponse = ErrorResponse.builder()
                        .success(false)
                        .message("실패")
                        .data(errorMessage).build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            accountService.signup(accountDto);

            ControllerApiResponse<Object> response = ControllerApiResponse.builder()
                    .success(true)
                    .message("회원가입 성공").build();
            return ResponseEntity.ok(response);

        }catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }catch (NumberFormatException e){
            throw new BadRequestException("숫자만 입력해주세요");
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException("서버오류 발생",e);
        }
    }
}