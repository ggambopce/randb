package com.jinho.randb.domain.post.dao;


import com.jinho.randb.domain.post.domain.QPost;
import com.jinho.randb.domain.post.dto.PostDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {
    
    private final JPAQueryFactory queryFactory;

    /**
     * 동적(주제) 무한 스크르롤 페이징 기능
     * @param postTitle      String 토론주제
     * @param pageable          페이징
     * @return          `        Slice반환
     */
    @Override
    public Slice<PostDto> getPost(String postTitle, Long lastPostId, Pageable pageable) {

        // 동적 쿼리 생성: 제목에 대한 LIKE 조건 추가
        BooleanBuilder builder = new BooleanBuilder();
        if (postTitle != null && !postTitle.isEmpty()) {
            // postTitle이 주어졌다면 해당 제목을 포함하는 게시글을 검색
            builder.and(QPost.post.postTitle.like("%" + postTitle + "%"));
        }
        // 마지막 레시피 아이디 값을 동해 페이지 유뮤 판단
        if (lastPostId!=null){
            builder.and(QPost.post.id.gt(lastPostId));
        }

        // QueryDSL을 사용하여 동적 쿼리 실행
        List<Tuple> result = getSearchPost(pageable, builder);

        // Tuple 결과를 PostDto 리스트로 변환
        List<PostDto> content = getPostDtoList(result);

        // 다음 페이지가 있는지 확인 (현재 페이지 크기와 일치하면 더 많은 데이터가 있다고 판단)
        boolean hasNext = isHasNext(pageable, content);

        // SliceImpl을 반환하여 페이징 처리된 결과를 클라이언트에 제공
        return new SliceImpl<>(content, pageable, hasNext);
    }

    private List<Tuple> getSearchPost(Pageable pageable, BooleanBuilder builder) {
        QPost post = QPost.post;

        // QueryDSL을 사용하여 동적 쿼리 실행
        List<Tuple> result = queryFactory.select(post.id, post.postTitle, post.postContent)
                .from(post)
                .where(builder)
                .orderBy(post.id.asc()) // 게시글 ID를 기준으로 오름차순 정렬
                .limit(pageable.getPageSize() + 1) // 페이징: +1을 추가하여 hasNext 여부를 판단
                .fetch();

        return result;
    }

    private List<PostDto> getPostDtoList(List<Tuple> result) {
        // Tuple 데이터를 PostDto로 변환
        List<PostDto> content = result.stream()
                .map(tuple -> new PostDto(
                        tuple.get(QPost.post.id),
                        tuple.get(QPost.post.postTitle),
                        tuple.get(QPost.post.postContent)))
                .collect(Collectors.toList());

        return content;
    }

    private static boolean isHasNext(Pageable pageable, List<PostDto> content) {
        boolean hasNext = false;

        // 페이지 크기보다 더 많은 데이터를 가져왔다면, 다음 페이지가 있다고 판단
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize()); // 마지막 데이터는 다음 페이지 확인용이므로 제거
            hasNext = true;
        }

        return hasNext;
    }





}
