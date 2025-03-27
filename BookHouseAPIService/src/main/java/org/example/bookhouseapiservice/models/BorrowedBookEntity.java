package org.example.bookhouseapiservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "borrowed_books")
@AllArgsConstructor
@NoArgsConstructor
public class BorrowedBookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    private LocalDateTime borrowedAt = LocalDateTime.now();
    private LocalDateTime returnDate = LocalDateTime.now().plusMonths(1);
    private boolean returned = false;
}
