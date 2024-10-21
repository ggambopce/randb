package com.jinho.randb.domain.opinionsummary.api;

import com.jinho.randb.domain.opinion.application.OpinionService;
import com.jinho.randb.domain.opinion.domain.OpinionType;
import com.jinho.randb.domain.opinion.dto.OpinionContentAndTypeDto;
import com.jinho.randb.domain.opinion.dto.OpinionDto;
import com.jinho.randb.global.payload.ControllerApiResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@OpenAPIDefinition(tags = {
        @Tag(name = "일반 사용자 의견 컨트롤러", description = "일반 사용자 의견 관련 작업")
})
@Slf4j
public class OpinionSummaryController {

    private final OpinionService opinionService;
    private final OpenAiChatModel openAiChatModel;
    //private final VertexAiGeminiChatModel vertexAiGeminiChatModel;

    @Operation(summary = "의견 전체 조회 및 요약 API", description = "의견의 전체 목록을 조회한 후 RED와 BLUE 입장을 각각 요약할 수 있습니다.", tags = {"일반 사용자 의견 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\" : \"조회 및 요약 성공\",\"summary\": \"[요약된 내용]\"}")))
    })
    @GetMapping("/opinions/summary")
    public ResponseEntity<?> summarizeOpinions(@Parameter(description = "토론글 ID") @RequestParam(value = "postId", required = false) Long postId) {
        // 이미 OpinionDto로 변환된 데이터를 가져오기 때문에 다시 변환하지 않음
        List<OpinionContentAndTypeDto> opinionDtos = opinionService.findByPostId(postId);

        // RED 의견 필터링
        List<String> redOpinions = opinionDtos.stream()
                .filter(opinion -> opinion.getOpinionType() == OpinionType.RED)
                .map(OpinionContentAndTypeDto::getOpinionContent)
                .collect(Collectors.toList());

        // BLUE 의견 필터링
        List<String> blueOpinions = opinionDtos.stream()
                .filter(opinion -> opinion.getOpinionType() == OpinionType.BLUE)
                .map(OpinionContentAndTypeDto::getOpinionContent)
                .collect(Collectors.toList());

        // RED 의견 요약
        String redPrompt = "다음은 RED 입장의 의견 목록입니다. 비속어나 비방하는 부분은 제외하고 논리정연하게 이 의견들을 요약하여 하나의 논설문으로 만들어 주세요:\n" + String.join("\n", redOpinions);
        String redSummary = openAiChatModel.call(redPrompt);

        // BLUE 의견 요약
        String bluePrompt = "다음은 BLUE 입장의 의견 목록입니다. 비속어나 비방하는 부분은 제외하고 논리정연하게 이 의견들을 요약하여 하나의 논설문으로 만들어 주세요:\n" + String.join("\n", blueOpinions);
        String blueSummary = openAiChatModel.call(bluePrompt);

        // 응답 생성
        Map<String, String> response = new HashMap<>();
        response.put("red_summary", redSummary);
        response.put("blue_summary", blueSummary);

        return ResponseEntity.ok(new ControllerApiResponse<>(true, "요약 성공", response));
    }
}
