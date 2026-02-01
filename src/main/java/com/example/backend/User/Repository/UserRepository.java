package com.example.backend.User.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.User.Model.UserDB;

public interface UserRepository extends JpaRepository<UserDB,UUID> {
    
}
