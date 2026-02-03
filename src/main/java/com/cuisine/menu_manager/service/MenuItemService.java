package com.cuisine.menu_manager.service;

import java.util.List;

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

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public MenuItem getMenuItemById(String id) {
        return menuItemRepository.findById(id).orElse(null);
    }

    public MenuItem createMenuItem(MenuItem menuItem) {
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

    public List<MenuItem> searchMenuItems(String keyword, String category, Double minPrice, Double maxPrice) {
        Query query = new Query();

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

        return mongoTemplate.find(query, MenuItem.class);
    }
}
