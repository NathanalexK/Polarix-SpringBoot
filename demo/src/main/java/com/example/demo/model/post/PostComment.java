package com.example.demo.model.post;

import com.example.demo.dto.post.SendCommentDTO;
import com.example.demo.model.user.AppUser;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "post_comment")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sender")
    private AppUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_post")
    private Post post;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "content")
    private String content;

    public PostComment() {
    }

}
