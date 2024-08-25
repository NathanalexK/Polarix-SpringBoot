package com.example.demo.service.post;

import com.example.demo.dto.friend.FriendRowDTO;
import com.example.demo.dto.post.*;
import com.example.demo.exception.CustomHttpException;
import com.example.demo.model.misc.Notification;
import com.example.demo.model.post.Post;
import com.example.demo.model.post.PostComment;
import com.example.demo.model.post.PostLike;
import com.example.demo.model.user.AppUser;
import com.example.demo.model.user.Privacy;
import com.example.demo.repository.post.PostCommentRepository;
import com.example.demo.repository.post.PostLikeRepository;
import com.example.demo.repository.post.PostRepository;
import com.example.demo.repository.user.AppUserRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
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
    private final PostLikeRepository postLikeRepository;
    private final PostCommentRepository postCommentRepository;
    private final AppUserRepository appUserRepository;

    public PostService(AuthService authService, ImageUploaderService imageUploaderService,
                       PostRepository postRepository, FriendRepository friendRepository, FriendService friendService, WebSocketService webSocketService, PostLikeRepository postLikeRepository,
                       PostCommentRepository postCommentRepository, AppUserRepository appUserRepository) {
        this.authService = authService;
        this.imageUploaderService = imageUploaderService;
        this.postRepository = postRepository;
        this.friendRepository = friendRepository;
        this.friendService = friendService;
        this.webSocketService = webSocketService;
        this.postLikeRepository = postLikeRepository;
        this.postCommentRepository = postCommentRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public Post savePost(PostPublicationDTO postPublicationDTO) throws IOException {
        AppUser user = authService.getAuthenticatedAppUser();

        Post post = new Post();
        post.setUser(user);
        post.setText(postPublicationDTO.getText());
        post.setDate(LocalDateTime.now());
        post.setPrivacy(Privacy.valueOf(postPublicationDTO.getPrivacy().toUpperCase()));
        post.setLikeCount(0);
        post.setCommentCount(0);

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

    public PostDetailsDTO getPostById(Integer idUser, Integer idPost) {
        return postRepository.findPostByIdWithLikeStatus(idUser, idPost);
    }

    public PostDetailsDTO getPostById(Integer idPost) {
        AppUser current = authService.getAuthenticatedAppUser();
        return getPostById(current.getId(), idPost);
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

    public Pagination<PostDetailsDTO> getPostPageableWithLikeStatus(Integer pageNumber, Integer pageSize){
        AppUser user = authService.getAuthenticatedAppUser();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostDetailsDTO> page = postRepository.findAllPostPageableWithLikeStatus(user.getId(), pageable);
        return new Pagination<PostDetailsDTO>(page);
    }

    @Transactional
    public PostLikeActionDTO likePost(Integer idPost){
        AppUser user = authService.getAuthenticatedAppUser();
        Post post = postRepository.findById(idPost).orElse(null);

        if(post == null) throw new CustomHttpException("Invalid post id!", HttpStatus.INTERNAL_SERVER_ERROR);

        PostLike postLike = postLikeRepository.findPostLikeByPostAndUser(post.getId(), user.getId());
        PostLikeActionDTO action = new PostLikeActionDTO();
        action.setIdPost(post.getId());

        if(postLike == null) {
            postLike = new PostLike(user, post);
            postLikeRepository.save(postLike);
            postRepository.incrementLike(post.getId());
            action.setIsLike(true);
            action.setLikeCount(post.getLikeCount() + 1);
            webSocketService.sendNotification(
                    "/topic/user/" + post.getUser().getUsername(),
                    Notification.info("Post Like", String.format("<b>%s</b> Liked your Post!", user.getUsername()))
            );
        }
        else {
            postLikeRepository.delete(postLike);
            postRepository.decrementLike(post.getId());
            action.setIsLike(false);
            action.setLikeCount(post.getLikeCount() - 1);
        }
        return action;
    }

    public Pagination<PostDetailsDTO> getAllPostFromUserWithStatusPageable(String viewerUsername, String currentUsername, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostDetailsDTO> page = postRepository.findPostByUserWithLikeStatusPageable(viewerUsername, currentUsername, pageable);
        return new Pagination<PostDetailsDTO>(page);
    }

    public Pagination<PostDetailsDTO> getAllPostFromUserWithStatusPageable(String username, Integer pageNumber, Integer pageSize) {
        Authentication auth = authService.getAuthenticated();
        return getAllPostFromUserWithStatusPageable(auth.getName(), username, pageNumber, pageSize);
    }

    public Pagination<PostCommentDTO> getPostCommentByIdPost(Integer idPost, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return new Pagination<PostCommentDTO>(postCommentRepository.findPostCommentByIdPostPageable(idPost, pageable));
    }

    @Transactional
    public PostCommentDTO sendComment(AppUser currentUser, SendCommentDTO comment){
        Post post = postRepository.findById(comment.getIdPost()).orElseThrow();

        PostComment postComment = new PostComment();
        postComment.setSender(currentUser);
        postComment.setPost(post);
        postComment.setDate(LocalDate.now());
        postComment.setContent(comment.getContent());

        PostComment savedPost = postCommentRepository.save(postComment);
        postRepository.incrementComment(post.getId());
        webSocketService.sendNotification(
                "/topic/user/" + post.getUser().getUsername(),
                Notification.info("Post", "<b>" + currentUser.getUsername() + "</b> commented your post!")
        );

        return new PostCommentDTO(postComment);
    }

    @Transactional
    public PostCommentDTO sendComment(SendCommentDTO comment) {
        AppUser currentUser = authService.getAuthenticatedAppUser();
        return sendComment(currentUser, comment);
    }
}
