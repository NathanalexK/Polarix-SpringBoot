package com.example.demo.model.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Entity
@Table(
        name = "friend",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id_sender", "id_receiver"})
        }
)
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

    @Column(name = "date_send", nullable = false)
    private LocalDate dateSend;

    @Column(name = "date_confirm")
    private LocalDate dateConfirm;
}
