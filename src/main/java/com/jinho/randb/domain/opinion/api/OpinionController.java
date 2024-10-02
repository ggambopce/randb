package com.jinho.randb.domain.opinion.api;

import com.jinho.randb.domain.opinion.application.OpinionService;
import com.jinho.randb.domain.opinion.dto.AddOpinionRequest;
import com.jinho.randb.global.payload.ControllerApiResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@OpenAPIDefinition(tags = {
        @Tag(name = "일반 사용자 의견 컨트롤러", description = "일반 사용자 의견 관련 작업")
})
@Slf4j
public class OpinionController {

    private final OpinionService opinionService;

    @PostMapping("/api/user/opinions")
    public ResponseEntity<?> opinionAdd(@Valid @RequestBody AddOpinionRequest addOpinionRequest){

        opinionService.save(addOpinionRequest);

        return ResponseEntity.ok(new ControllerApiResponse(true,"작성 성공"));
    }
}