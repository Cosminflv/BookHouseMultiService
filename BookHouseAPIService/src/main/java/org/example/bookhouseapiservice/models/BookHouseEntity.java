package org.example.bookhouseapiservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "book_house")
public class BookHouseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;

    @OneToMany(mappedBy = "bookHouseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserEntity> users;

    @OneToMany(mappedBy = "bookHouseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookEntity> books;
}
