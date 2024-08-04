package com.example.demo.api.post;

import com.example.demo.dto.post.PostLikeActionDTO;
import com.example.demo.service.post.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post/like")
public class PostLikeController {

    private final PostService postService;

    public PostLikeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostLikeActionDTO> likePost(@PathVariable(name = "id", required = true) Integer id){
        PostLikeActionDTO likeActionDTO = postService.likePost(id);
        return ResponseEntity.ok(likeActionDTO);
    }
}
