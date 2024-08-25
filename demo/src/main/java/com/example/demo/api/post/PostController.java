package com.example.demo.api.post;

import com.example.demo.dto.notif.NotificationDTO;
import com.example.demo.dto.post.PostDetailsDTO;
import com.example.demo.dto.post.PostPublicationDTO;
import com.example.demo.model.misc.Notification;
import com.example.demo.model.post.Post;
import com.example.demo.model.user.Friend;
import com.example.demo.service.post.PostService;
import com.example.demo.util.Pagination;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Pagination<PostDetailsDTO>> getAllPost(
            @PathVariable("pageNumber") Integer pageNumber,
            @PathVariable("pageSize") Integer pageSize
    ) throws InterruptedException {

//        Thread.sleep(500);
        Pagination<PostDetailsDTO> pagination = postService.getPostPageableWithLikeStatus(pageNumber, pageSize);
        return ResponseEntity.ok(pagination);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<PostDetailsDTO> getPost(@NotNull @PathVariable("id") Integer idPost) {
        PostDetailsDTO postDetailsDTO = postService.getPostById(idPost);
        return ResponseEntity.ok(postDetailsDTO);
    }

    @GetMapping("user/{username}/{pageNumber}/{pageSize}")
    public ResponseEntity<Pagination<PostDetailsDTO>> getAllPostByUser(
            @NotNull @PathVariable("username") String username,
            @NotNull @PathVariable("pageNumber") Integer pageNumber,
            @NotNull @PathVariable("pageSize") Integer pageSize
    ) {
        Pagination<PostDetailsDTO> pagination = postService.getAllPostFromUserWithStatusPageable(username, pageNumber, pageSize);
        return ResponseEntity.ok(pagination);
    }

    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public ResponseEntity<NotificationDTO> postPublication(
            @RequestParam("text") String text,
            @RequestParam("privacy") String privacy,
            @RequestParam(value = "picture", required = false) MultipartFile picture
    )
            throws IOException {
        Post post = postService.savePost(new PostPublicationDTO(text, picture, privacy));
        return ResponseEntity.ok(new NotificationDTO(Notification.success("Post", "Your post successfulty posted!")));
    }
}

