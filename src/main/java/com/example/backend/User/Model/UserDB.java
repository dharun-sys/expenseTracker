package com.example.backend.User.Model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Table(name = "accountdb") //accountDB
@NoArgsConstructor
@Data
@Builder

public class UserDB {
       @Id
    
    @GeneratedValue(strategy = GenerationType.UUID) 
    private UUID id;

    @Column
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique =true,length = 15)
    private String phone;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

  
    @Column(nullable = false)
    @Builder.Default
    private boolean status = false;

   

    public boolean getIsActive() {
        return status;
    }
}
    

