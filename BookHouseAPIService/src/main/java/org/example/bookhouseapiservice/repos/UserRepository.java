package org.example.bookhouseapiservice.repos;

import org.example.bookhouseapiservice.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // Additional query methods if needed
}
