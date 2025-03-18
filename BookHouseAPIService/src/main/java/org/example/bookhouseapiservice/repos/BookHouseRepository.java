package org.example.bookhouseapiservice.repos;

import org.example.bookhouseapiservice.models.BookHouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookHouseRepository extends JpaRepository<BookHouseEntity, Long> {
}
