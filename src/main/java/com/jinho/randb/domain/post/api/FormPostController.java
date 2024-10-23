package com.jinho.randb.domain.post.api;

import com.jinho.randb.domain.post.application.PostService;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.dto.user.MainPagePostResponse;
import com.jinho.randb.domain.post.dto.user.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class FormPostController {

    private final PostService postService;

    @GetMapping("/main")
    public String showMainPage(Model model) {
        // 서비스에서 메인 페이지 게시글 목록을 가져옴
        MainPagePostResponse mainPagePostResponse = postService.mainPagePost();

        // 모델에 데이터를 추가하여 Thymeleaf 템플릿에서 사용
        model.addAttribute("posts", mainPagePostResponse.getPosts());
        return "rest/main"; // main.html 템플릿
    }

    @GetMapping("/posts")
    public String showPosts(@RequestParam(value = "postId", required = false) Long postId, Model model, Pageable pageable) {

        PostResponse postResponse = postService.postPage(postId,pageable);

        // 모델에 데이터를 추가하여 Thymeleaf 템플릿에서 사용
        model.addAttribute("posts", postResponse.getPosts());
        model.addAttribute("page", postResponse.getNextPage());
        return "rest/post"; // Thymeleaf 템플릿
    }

    @GetMapping("/detailpost/{post-id}")
    public String findPost(@PathVariable("post-id") long id, Model model) {

        // id로 게시물 조회
        Optional<Post> post = postService.findById(id);

        // 게시물이 존재하는지 확인
        if (post.isPresent()) {
            // 모델에 게시물 데이터를 추가하여 Thymeleaf로 전달
            model.addAttribute("post", post.get());
            return "rest/detailpost"; // detailpost.html 템플릿 반환
        } else {
            // 게시물이 없을 경우 에러 페이지 또는 메시지 전달
            model.addAttribute("errorMessage", "해당 게시물을 찾을 수 없습니다.");
            return "error"; // error.html 템플릿으로 이동
        }
    }

}
