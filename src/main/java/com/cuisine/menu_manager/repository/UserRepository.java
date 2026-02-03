package com.cuisine.menu_manager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cuisine.menu_manager.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Spring auto-generates the query from the method name:
    // this becomes: db.users.find({ "username": username })
    User findByUsername(String username);

    // this becomes: db.users.count({ "username": username }) > 0
    boolean existsByUsername(String username);
}
