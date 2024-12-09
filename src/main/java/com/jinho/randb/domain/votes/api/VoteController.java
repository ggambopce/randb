package com.jinho.randb.domain.votes.api;

import com.jinho.randb.domain.votes.application.VoteService;
import com.jinho.randb.domain.votes.dto.VoteRequestDto;
import com.jinho.randb.domain.votes.dto.VoteResultDto;
import com.jinho.randb.global.payload.ControllerApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class VoteController {

    private final VoteService voteService;

    @PostMapping("api/user/posts/votes")
    public ResponseEntity<?> addVote(@RequestBody VoteRequestDto voteRequest) {
        try {
            voteService.saveVote(voteRequest);
            return ResponseEntity.ok(new ControllerApiResponse<>(true, "투표가 성공적으로 처리되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ControllerApiResponse<>(false, e.getMessage()));
        }
    }

    // 투표 결과 조회 API
    @GetMapping("api/user/posts/votes/{postId}")
    public ResponseEntity<?> getVoteResults(@PathVariable("postId") Long postId) {
        VoteResultDto results = voteService.getVoteResults(postId);
        return ResponseEntity.ok(new ControllerApiResponse<>(true, "투표 결과 조회 성공", results));
    }


}
