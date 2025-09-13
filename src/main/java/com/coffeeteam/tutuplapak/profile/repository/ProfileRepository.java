package com.coffeeteam.tutuplapak.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coffeeteam.tutuplapak.core.entity.User;

public interface ProfileRepository extends JpaRepository<User, Long> {
    
}
