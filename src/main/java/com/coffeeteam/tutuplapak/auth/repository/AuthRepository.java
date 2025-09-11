package com.coffeeteam.tutuplapak.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coffeeteam.tutuplapak.core.entity.User;

@Repository
public interface AuthRepository extends JpaRepository<User, Long>{

    @Query("""
        SELECT 
            u
        FROM 
            User u
        WHERE
            u.email = :email        
    """
    )
    Optional<User> findByEmail(@Param("email") String email);

    @Query("""
        SELECT 
            u
        FROM 
            User u
        WHERE
            u.phone = :phone        
    """
    )
    Optional<User> findByPhone(@Param("phone") String phone);

    @Query("""
        SELECT 
            u
        FROM 
            User u
        WHERE
            u.phone = :phone
            AND u.email = :email        
    """
    )
    Optional<User> findByPhoneAndEmail(@Param("phone") String phone, @Param("email") String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String email);
}
