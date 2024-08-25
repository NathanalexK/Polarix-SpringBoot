package com.example.demo.dto.post;

import com.example.demo.model.post.PostComment;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PostCommentDTO {
    private String username;
    private String name;
    private String userPicture;
    private String date;
    private String content;

    public PostCommentDTO() {
    }

    public PostCommentDTO(String username, String name, String userPicture, LocalDate date, String content) {
        this.username = username;
        this.name = name;
        this.userPicture = userPicture;
        this.date = String.valueOf(date);
        this.content = content;
    }

    public PostCommentDTO(PostComment pc) {
        this.username = pc.getSender().getUsername();
        this.name = pc.getSender().getName();
        this.userPicture = pc.getSender().getPicture();
        this.date = String.valueOf(pc.getDate());
        this.content = pc.getContent();
    }
}
