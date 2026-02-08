package com.cuisine.menu_manager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cuisine.menu_manager.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);

    boolean existsByUsername(String username);
}
