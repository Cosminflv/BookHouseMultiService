package org.example.bookhouseapiservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private int totalStock;

    @ManyToOne
    @JoinColumn(name = "bookhouse_id", nullable = false)
    private BookHouseEntity bookHouseEntity;
}
