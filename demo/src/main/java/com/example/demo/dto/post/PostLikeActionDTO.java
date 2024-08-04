package com.example.demo.dto.post;

import lombok.Data;

@Data
public class PostLikeActionDTO {
    private Integer idPost;
    private Boolean isLike;
    private Integer likeCount;

    public PostLikeActionDTO() {
    }

    public PostLikeActionDTO(Integer idPost, Boolean isLike) {
        this.idPost = idPost;
        this.isLike = isLike;
    }
}
