package com.example.demo.model.post;


import com.example.demo.model.user.AppUser;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "post_like")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_post")
    private Post post;

    @Column(name = "date")
    private LocalDate date;

    public PostLike(){
    }

    public PostLike(AppUser user, Post post) {
        this.user = user;
        this.post = post;
        this.date = LocalDate.now();
    }
}
