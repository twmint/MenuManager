package com.cuisine.menu_manager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cuisine.menu_manager.model.MenuItem;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    Page<MenuItem> findByUserId(String userId, Pageable pageable);
}
