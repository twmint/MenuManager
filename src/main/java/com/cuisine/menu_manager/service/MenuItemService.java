package com.cuisine.menu_manager.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.cuisine.menu_manager.model.MenuItem;
import com.cuisine.menu_manager.repository.MenuItemRepository;

@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MongoTemplate mongoTemplate;

    public MenuItemService(MenuItemRepository menuItemRepository, MongoTemplate mongoTemplate) {
        this.menuItemRepository = menuItemRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Page<MenuItem> getAllMenuItems(String userId, int page, int size ) {
        return menuItemRepository.findByUserId(userId, PageRequest.of(page - 1, size));
    }

    public MenuItem getMenuItemById(String id) {
        return menuItemRepository.findById(id).orElse(null);
    }

    public MenuItem createMenuItem(String userId, MenuItem menuItem) {
        menuItem.setUserId(userId);
        return menuItemRepository.save(menuItem);
    }

    public MenuItem updateMenuItem(String id, MenuItem menuItem) {
        MenuItem existingMenuItem = menuItemRepository.findById(id).orElse(null);
        if (existingMenuItem == null) {
            return null;
        }
        existingMenuItem.setName(menuItem.getName());
        existingMenuItem.setDescription(menuItem.getDescription());
        existingMenuItem.setPrice(menuItem.getPrice());
        existingMenuItem.setCategory(menuItem.getCategory());
        existingMenuItem.setImageUrl(menuItem.getImageUrl());
        return menuItemRepository.save(existingMenuItem);
    }

    public void deleteMenuItem(String id) {
        menuItemRepository.deleteById(id);
    }

    public Page<MenuItem> searchMenuItems(String userId, String keyword, String category, Double minPrice, Double maxPrice, int page, int size) {
        Query query = new Query();

        if (userId != null) {
            query.addCriteria(Criteria.where("userId").is(userId));
        } else {
            return new PageImpl<>(List.of());
        }

        if (keyword != null) {
            query.addCriteria(Criteria.where("name").regex(keyword, "i"));
        }
        if (category != null) {
            query.addCriteria(Criteria.where("category").is(category));
        }
        if (minPrice != null && maxPrice != null) {
            query.addCriteria(Criteria.where("price").gte(minPrice).lte(maxPrice));
        } else if (minPrice != null) {
            query.addCriteria(Criteria.where("price").gte(minPrice));
        } else if (maxPrice != null) {
            query.addCriteria(Criteria.where("price").lte(maxPrice));
        }
        
        Pageable pageable = PageRequest.of(page - 1, size);   
        query.with(pageable);
        List<MenuItem> items = mongoTemplate.find(query, MenuItem.class);
        long count = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), MenuItem.class);
        return new PageImpl<>(items, pageable, count);
    }
}
