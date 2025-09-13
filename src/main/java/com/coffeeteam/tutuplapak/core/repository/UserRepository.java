package com.coffeeteam.tutuplapak.core.repository;


import com.coffeeteam.tutuplapak.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
