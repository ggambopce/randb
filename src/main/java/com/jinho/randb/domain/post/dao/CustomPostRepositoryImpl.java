package com.jinho.randb.domain.post.dao;


import com.jinho.randb.domain.post.domain.Post;
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

import static com.jinho.randb.domain.post.domain.QPost.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {
    
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 해당 id 토론글 데이터를 가져와서 RecipeDto로 변환
     * @param postId
     * @return postDTO
     */
    @Override
    public PostDto getPostDetail(Long postId) {

        List<Tuple> postDetail = jpaQueryFactory
                .select(post.id, post.postTitle, post.postContent)
                .from(post)
                .where(post.id.eq(postId))
                .fetch();

        Post postEntity = postDetail.stream().map(tuple -> tuple.get(post)).collect(Collectors.toList()).stream().findFirst().get();

        return PostDto.of(postEntity);

    }

    /**
     * 게시글을 페이지네이션 방식으로 페이징 처리 (no-offset 방식 사용)
     * @param postId 마지막으로 조회된 게시글의 ID
     * @param pageable 페이지 정보
     * @return Slice<PostDto> 페이징 결과
     */
    @Override
    public Slice<PostDto> getAllPost(Long postId, Pageable pageable) {

        // 동적 쿼리: postId가 주어졌다면 해당 ID 이후의 게시글을 조회하는 조건
        // BooleanBuilder는 동적 쿼리 조건을 쌓는 데 사용
        BooleanBuilder builder = new BooleanBuilder();
        if (postId != null) {
            builder.and(post.id.gt(postId)); // postId 이후의 게시글을 조회
        }

        // QueryDSL을 사용하여 동적 쿼리 실행 및 페이징 처리
        List<Tuple> list = jpaQueryFactory.select(post.id, post.postTitle, post.postContent, post.createdAt)
                .from(post)
                .orderBy(post.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        // Tuple 데이터를 PostDto로 변환하여 리스트로 수집
        List<PostDto> collect = list.stream()
                .map(tuple -> new PostDto(tuple.get(post.id),tuple.get(post.postTitle),tuple.get(post.postContent))).collect(Collectors.toList());

        // 다음 페이지가 있는지 확인
        boolean hasNext = isHasNextSize(pageable, collect);

        // SliceImpl을 반환하여 페이징 처리된 결과를 클라이언트에 제공
        // Slice는 Page와 달리 전체 데이터 개수를 신경 쓰지 않고, 단순히 다음 페이지가 있는지만 확인
        return new SliceImpl<>(collect, pageable, hasNext);
    }

    @Override
    public List<PostDto> mainPagePost() {

        //튜플로 토론글 id, 제목, 내용을 조회
        List<Tuple> list = jpaQueryFactory.select(post.id, post.postTitle, post.postContent)
                .from(post)
                .fetch();
        return list.stream().map(tuple -> PostDto.from(tuple.get(post.id),
                tuple.get(post.postTitle),
                tuple.get(post.postContent))).collect(Collectors.toList());
    } //from은 정적 팩토리 메서드로 new 키워드를 사용하는 것과는 다른 방식. 팩토리 메서드는 new 없이 호출


    private static boolean isHasNextSize(Pageable pageable, List<PostDto> collect) {
        boolean hasNextSize = false;
        if(collect.size()> pageable.getPageSize()){
            collect.remove(pageable.getPageSize());
            hasNextSize = true;
        }
        return hasNextSize;
    }



}
