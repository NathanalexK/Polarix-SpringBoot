package com.example.demo.service.post;

import com.example.demo.dto.friend.FriendRowDTO;
import com.example.demo.dto.post.PostDetailsDTO;
import com.example.demo.dto.post.PostPublicationDTO;
import com.example.demo.model.misc.Notification;
import com.example.demo.model.post.Post;
import com.example.demo.model.user.AppUser;
import com.example.demo.model.user.Friend;
import com.example.demo.model.user.Privacy;
import com.example.demo.repository.post.PostRepository;
import com.example.demo.repository.user.FriendRepository;
import com.example.demo.service.user.AuthService;
import com.example.demo.service.user.FriendService;
import com.example.demo.service.util.ImageUploaderService;
import com.example.demo.service.util.WebSocketService;
import com.example.demo.util.Pagination;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final AuthService authService;
    private final ImageUploaderService imageUploaderService;
    private final PostRepository postRepository;
    private final FriendRepository friendRepository;
    private final FriendService friendService;
    private final WebSocketService webSocketService;

    public PostService(AuthService authService, ImageUploaderService imageUploaderService,
                       PostRepository postRepository, FriendRepository friendRepository, FriendService friendService, WebSocketService webSocketService) {
        this.authService = authService;
        this.imageUploaderService = imageUploaderService;
        this.postRepository = postRepository;
        this.friendRepository = friendRepository;
        this.friendService = friendService;
        this.webSocketService = webSocketService;
    }

    @Transactional
    public Post savePost(PostPublicationDTO postPublicationDTO) throws IOException {
        AppUser user = authService.getAuthenticatedAppUser();

        Post post = new Post();
        post.setUser(user);
        post.setText(postPublicationDTO.getText());
        post.setDate(LocalDateTime.now());
        post.setPrivacy(Privacy.valueOf(postPublicationDTO.getPrivacy().toUpperCase()));

        if(postPublicationDTO.getPicture() != null) {
            String urlImage = imageUploaderService.uploadImage(postPublicationDTO.getPicture(), "/user/post");
            post.setPicture(urlImage);
        } else {
            post.setPicture(null);
        }

        Post savedPost = postRepository.save(post);

        for(FriendRowDTO friendRowDTO: friendService.getFriendListByUser(user.getUsername())){
            System.out.println(friendRowDTO.getUsername());
            webSocketService.sendNotification(
                    "/topic/user/" + friendRowDTO.getUsername(),
                    Notification.info("Post", "<b>" + user.getUsername() + "</b> just post a publication!")
            );
        }

        return savedPost;
    }

    public Page<Post> getAllPost(Integer pageNumber, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Post> postPage = postRepository.findAllPostPageable(pageable);
        return postPage;
    }

    public Pagination<PostDetailsDTO> getAllPostDTO(Integer pageNumber, Integer pageSize){
        List<PostDetailsDTO> list = new ArrayList<>();
        Page<Post> postPage = this.getAllPost(pageNumber, pageSize);

        for(Post post: postPage.getContent()){
            list.add(new PostDetailsDTO(post));
        }

        Pagination pagination = new Pagination();
        pagination.setData(list);
        pagination.setCurrentPage(postPage.getNumber());
        pagination.setTotalPages(postPage.getTotalPages());
        pagination.setTotalElements(postPage.getNumberOfElements());

        return pagination;
    }
}
