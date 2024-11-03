package com.jinho.randb.domain.admin.api;

import com.jinho.randb.domain.admin.application.AdminService;
import com.jinho.randb.global.payload.ControllerApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "어드민 관련 컨트롤러", description = "어드민 페이지 관련 API")
@RequestMapping
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "회원수 조회 API", description = "현재 가입된 회원수를 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\":\"조회 성공\", \"data\":\"10\"}"))),
    })
    @GetMapping("/api/accounts/count")
    public ResponseEntity<?> getAccountsCount(){
        long searchAccountsCount = adminService.searchAcountCount();
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "조회 성공", searchAccountsCount));
    }
}
