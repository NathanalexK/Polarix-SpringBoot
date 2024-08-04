package com.example.demo.dto.post;

import com.example.demo.model.post.Post;
import lombok.Data;

@Data
public class PostDetailsDTO {
    private String username;
    private String userPicture;
    private String date;
    private String text;
    private String postPicture;

    public PostDetailsDTO() {
    }

    public PostDetailsDTO(Post post){
        this.setUsername(post.getUser().getUsername());
        this.setUserPicture(post.getUser().getPicture());
        this.setDate(String.valueOf(post.getDate()));
        this.setText(post.getText());
        this.setPostPicture(post.getPicture());
    }
}
