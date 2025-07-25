package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findUserById(Long id);

    // Custom query to fetch a user by ID with their subscriptions
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.subscriptions WHERE u.id = :userId")
    Optional<User> findByIdWithSubscriptions(@Param("userId") Long userId);

}


