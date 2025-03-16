package org.example.bookhouseapi.repos;

import org.example.bookhouseapi.models.BookHouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookHouseRepository extends JpaRepository<BookHouseEntity, Long> {
}
