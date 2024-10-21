package com.jinho.randb.domain.account.api;

import com.jinho.randb.domain.account.application.AccountService;
import com.jinho.randb.domain.account.dto.AccountDto;
import com.jinho.randb.global.exception.ErrorResponse;
import com.jinho.randb.global.exception.ex.BadRequestException;
import com.jinho.randb.global.payload.ControllerApiResponse;
import com.jinho.randb.global.security.provider.RestAuthenticationProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
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
    private final AuthenticationManager authenticationManager;

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

    @Operation(summary = "로그인", description = "사용자가 세션을 통해 로그인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\": \"로그인 성공\"}"))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\":false,\"message\":\"로그인 실패: 자격 증명이 유효하지 않습니다.\"}"))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login/login")
    public ResponseEntity<?> login(@Valid @RequestBody AccountDto accountDto, BindingResult result, HttpServletRequest request) {
        try {
            if (result.hasErrors()) {
                Map<String, String> errorMap = new HashMap<>();
                for (FieldError error : result.getFieldErrors()) {
                    errorMap.put(error.getField(), error.getDefaultMessage());
                }
                ControllerApiResponse<Object> failResponse = ControllerApiResponse.builder()
                        .success(false)
                        .message("실패")
                        .data(errorMap).build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failResponse);
            }
            // 로그인 처리 로직
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(accountDto.getUsername(), accountDto.getPassword());

            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 세션 생성 및 보안 컨텍스트 저장
            HttpSession session = request.getSession(true); // 세션이 없으면 새로 생성
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

            // 세션 ID를 클라이언트에 반환 (세션 토큰으로 사용할 수 있음)
            String sessionId = session.getId();

            ControllerApiResponse<Object> response = ControllerApiResponse.builder()
                    .success(true)
                    .message("로그인 성공")
                    .data(sessionId)  // 세션 ID를 클라이언트로 반환하여 세션 토큰으로 사용
                    .build();
            return ResponseEntity.ok(response);

            }catch (BadRequestException e){
                throw new BadRequestException(e.getMessage());
            }catch (AccessDeniedException e){
                throw new AccessDeniedException(e.getMessage());
            }
        catch (Exception e){
                e.printStackTrace();
                throw new ServerErrorException(e.getMessage(),e);
            }
    }
    @Operation(summary = "로그아웃", description = "사용자가 세션을 통해 로그아웃합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\": \"로그아웃 성공\"}"))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\":false,\"message\":\"서버 오류로 로그아웃 실패\"}")))
    })
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        ControllerApiResponse<Object> responseBody = ControllerApiResponse.builder()
                .success(true)
                .message("로그아웃 성공")
                .build();

        return ResponseEntity.ok(responseBody);
    }
}

