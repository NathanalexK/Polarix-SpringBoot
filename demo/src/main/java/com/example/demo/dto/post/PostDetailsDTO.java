package com.example.demo.dto.post;

import com.example.demo.model.post.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDetailsDTO {
    private Integer id;
    private String username;
    private String userPicture;
    private String date;
    private String text;
    private String postPicture;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean isLiked;

    public PostDetailsDTO() {
    }

    public PostDetailsDTO(Post post) {
        this.setId(post.getId());
        this.setUsername(post.getUser().getUsername());
        this.setUserPicture(post.getUser().getPicture());
        this.setDate(String.valueOf(post.getDate()));
        this.setText(post.getText());
        this.setPostPicture(post.getPicture());
        this.setLikeCount(post.getLikeCount());
        this.setCommentCount(post.getCommentCount());
    }

    public PostDetailsDTO(Integer id,
                          String username,
                          String userPicture,
                          LocalDateTime date,
                          String text,
                          String postPicture,
                          Integer likeCount,
                          Integer commentCount,
                          Boolean isLiked
    ) {
        this.id = id;
        this.username = username;
        this.userPicture = userPicture;
        this.date = String.valueOf(date);
        this.text = text;
        this.postPicture = postPicture;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.isLiked = isLiked;
    }
}
