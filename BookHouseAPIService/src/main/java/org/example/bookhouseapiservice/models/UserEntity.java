package org.example.bookhouseapiservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.bookhouseapiservice.models.enums.EUserType;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private EUserType userType;

    @ManyToOne
    @JoinColumn(name = "bookhouse_id", nullable = true)
    private BookHouseEntity bookHouseEntity;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowedBookEntity> borrowedBooks;
}
