package com.jinho.randb.domain.votes.dto;

import com.jinho.randb.domain.votes.domain.VoteType;
import lombok.Data;

@Data
public class VoteRequestDto {
    private Long postId; // 투표할 토론글 ID
    private Long accountId; // 투표한 사용자 ID
    private VoteType voteType; // RED 또는 BLUE
}
