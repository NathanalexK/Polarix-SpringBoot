package com.example.demo.api.post;

import com.example.demo.dto.post.PostCommentDTO;
import com.example.demo.dto.post.SendCommentDTO;
import com.example.demo.repository.post.PostCommentRepository;
import com.example.demo.service.post.PostService;
import com.example.demo.util.Pagination;
import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post/comment")
public class PostCommentController {
    private final PostService postService;

    public PostCommentController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/list/{idPost}/{pageNumber}/{pageSize}")
    public ResponseEntity<Pagination<PostCommentDTO>> getAllComments(
            @PathVariable("idPost") Integer idPost,
            @PathVariable("pageNumber") Integer pageNumber,
            @PathVariable("pageSize") Integer pageSize
    ) throws Exception {
        Thread.sleep(500);
        Pagination<PostCommentDTO> comments = postService.getPostCommentByIdPost(idPost, pageNumber, pageSize);
        return ResponseEntity.ok(comments);
    }

    @PostMapping()
    public ResponseEntity<PostCommentDTO> sendComment(@NotNull @RequestBody SendCommentDTO commentDTO) {
        PostCommentDTO postCommentDTO = postService.sendComment(commentDTO);
        return ResponseEntity.ok(postCommentDTO);
    }
}
