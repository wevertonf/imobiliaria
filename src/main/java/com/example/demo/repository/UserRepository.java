package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.UserModel;


//MySQL -> ORM (Hibernate/JPA) | Repository -> Model -> Controller -> Frontend
//MySQL -> Repository (Model) -> Controller -> Frontend

public interface UserRepository extends JpaRepository<UserModel, Integer> {
    
}
