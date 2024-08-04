package com.example.demo.dto.post;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostPublicationDTO {
    private String date;
    private String text;
    private MultipartFile picture;
    private String privacy;

    public PostPublicationDTO(){}

    public PostPublicationDTO(String text, MultipartFile picture ,String privacy){
        this.setText(text);
        this.setPicture(picture);
        this.setPrivacy(privacy);
    }
}
