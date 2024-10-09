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

import static com.jinho.randb.domain.post.domain.QPost.post;

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
        QPost post = QPost.post;

        // 동적 쿼리: postId가 주어졌다면 해당 ID 이후의 게시글을 조회하는 조건
        BooleanBuilder builder = new BooleanBuilder();
        if (postId != null) {
            builder.and(post.id.gt(postId)); // postId 이후의 게시글을 조회
        }

        // QueryDSL을 사용하여 동적 쿼리 실행 및 페이징 처리
        List<PostDto> result = jpaQueryFactory
                .selectFrom(post)
                .where(builder)
                .orderBy(post.id.asc()) // 게시글 ID를 기준으로 오름차순 정렬
                .limit(pageable.getPageSize() + 1) // 다음 페이지가 있는지 판단하기 위해 페이지 크기 + 1만큼 조회
                .fetch()
                .stream()
                .map(p -> new PostDto(p.getId(), p.getPostTitle(), p.getPostContent()))
                .collect(Collectors.toList());

        // 다음 페이지가 있는지 확인
        boolean hasNext = isHasNextSize(pageable, result);

        // SliceImpl을 반환하여 페이징 처리된 결과를 클라이언트에 제공
        return new SliceImpl<>(result, pageable, hasNext);
    }

    @Override
    public List<PostDto> mainPagePost() {

        //튜플로 토론글 id, 제목, 내용을 조회
        List<Tuple> list = jpaQueryFactory.select(post.id, post.postTitle, post.postContent)
                .from(post)
                .fetch();
        return list.stream().map(tuple -> PostDto.from(tuple.get(post.id),tuple.get(post.postTitle),tuple.get(post.postContent))).collect(Collectors.toList());
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
