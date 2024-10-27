package com.jinho.randb.domain.opinionsummary.application;

import com.jinho.randb.domain.opinion.application.OpinionService;
import com.jinho.randb.domain.opinion.domain.OpinionType;
import com.jinho.randb.domain.opinion.dto.OpinionContentAndTypeDto;
import com.jinho.randb.domain.opinionsummary.dao.OpinionSummaryRepository;
import com.jinho.randb.domain.opinionsummary.domain.OpinionSummary;
import com.jinho.randb.domain.opinionsummary.domain.QOpinionSummary;
import com.jinho.randb.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class OpinionSummaryServiceImpl implements OpinionSummaryService {


    private final OpinionService opinionService;
    private final OpinionSummaryRepository opinionSummaryRepository;
    private final OpenAiChatModel openAiChatModel;

    @Override
    public Map<String, String> saveOpinionSummary(Long postId) {

        // 의견 데이터 조회
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

        // RED 의견 요약을 DB에 저장
        OpinionSummary redOpinionSummary = OpinionSummary.builder()
                .opinionSummaryContent(redSummary)
                .opinionType(OpinionType.RED)
                .created_at(LocalDateTime.now())
                .build();

        OpinionSummary blueOpinionSummary = OpinionSummary.builder()
                .opinionSummaryContent(blueSummary)
                .opinionType(OpinionType.BLUE)
                .created_at(LocalDateTime.now())
                .build();

        opinionSummaryRepository.save(redOpinionSummary);
        opinionSummaryRepository.save(blueOpinionSummary);

        // 요약된 내용을 Map으로 반환
        Map<String, String> response = new HashMap<>();
        response.put("RED", redSummary);
        response.put("BLUE", blueSummary);

        return response;
    }
}
