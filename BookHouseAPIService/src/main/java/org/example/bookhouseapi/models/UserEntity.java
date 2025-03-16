package org.example.bookhouseapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.bookhouseapi.models.enums.EUserType;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private Long id;

    private String username;

    private EUserType userType;

    @ManyToOne
    @JoinColumn(name = "bookhouse_id", nullable = true)
    private BookHouseEntity bookHouseEntity;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowedBookEntity> borrowedBooks;
}
