package com.coffeeteam.tutuplapak.profile.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coffeeteam.tutuplapak.core.entity.User;

public interface ProfileRepository extends JpaRepository<User, Long> {
    
    @Query("""
        SELECT
            u
        FROM
            User u
        LEFT JOIN FETCH
            u.image
        WHERE
            u.id = :userId
    """)
    public Optional<User> findByIdWithImage(@Param("userId") Long userId);
}
