package com.example.demo.model.user;

import com.example.demo.model.misc.TypeContent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sender", nullable = false)
    private AppUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_receiver", nullable = false)
    private AppUser receiver;

    @Column(name = "date_time")
    private LocalDateTime datetime;

    @Column(name = "type_content")
    private TypeContent typeContent;

    @Column(name = "content")
    private String content;
}
