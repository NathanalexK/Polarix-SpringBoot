package com.example.demo.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "friend")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sender", nullable = false)
    private AppUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_receiver", nullable = false)
    private AppUser receiver;

    @Column(name = "date")
    private LocalDate date;
}
