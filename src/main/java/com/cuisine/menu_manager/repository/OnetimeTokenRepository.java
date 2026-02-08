package com.cuisine.menu_manager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cuisine.menu_manager.model.OnetimeToken;

@Repository
public interface OnetimeTokenRepository extends MongoRepository<OnetimeToken, String> {
    OnetimeToken findByToken(String token);

    void deleteByUsername(String username);
}