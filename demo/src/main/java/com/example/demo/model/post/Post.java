package com.example.demo.model.post;

import com.example.demo.model.user.AppUser;
import com.example.demo.model.user.Privacy;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private AppUser user;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "text")
    private String text;

    @Column(name = "picture")
    private String picture;

    @Column(name = "id_privacy", nullable = false)
    private Privacy privacy;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "comment_count")
    private Integer commentCount;

    public boolean equals(Post p){
        return this.getId() == p.getId();
    }
}
