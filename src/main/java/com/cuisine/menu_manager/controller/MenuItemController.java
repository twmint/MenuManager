package com.cuisine.menu_manager.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cuisine.menu_manager.model.MenuItem;
import com.cuisine.menu_manager.service.MenuItemService;
import com.cuisine.menu_manager.service.TokenService;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final TokenService tokenService;

    public MenuItemController(MenuItemService menuItemService, TokenService tokenService) {
        this.menuItemService = menuItemService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<MenuItem> createMenuItem(@RequestHeader("Authorization") String authorizationHeader, @RequestBody MenuItem menuItem) {
        String userId = extractUserId(authorizationHeader);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        MenuItem createdMenuItem = menuItemService.createMenuItem(userId, menuItem);
        return ResponseEntity.ok(createdMenuItem);
    }

    @GetMapping
    public ResponseEntity<Page<MenuItem>> getAllMenuItems(
         @RequestHeader("Authorization") String authorizationHeader,
         @RequestParam(defaultValue = "1") int page,
         @RequestParam(defaultValue = "20") int size
    ) {
        String userId = extractUserId(authorizationHeader);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        Page<MenuItem> menuItems = menuItemService.getAllMenuItems(userId, page, size);
        return ResponseEntity.ok(menuItems);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable String id, @RequestBody MenuItem menuItem) {
        MenuItem updatedMenuItem = menuItemService.updateMenuItem(id, menuItem);
        return ResponseEntity.ok(updatedMenuItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable String id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable String id) {
        MenuItem menuItem = menuItemService.getMenuItemById(id);
        return ResponseEntity.ok(menuItem);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<MenuItem>> searchMenuItems(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        String userId = extractUserId(authorizationHeader);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        Page<MenuItem> menuItems = menuItemService.searchMenuItems(userId, keyword, category, minPrice, maxPrice, page, size);
        return ResponseEntity.ok(menuItems);
    }

    private String extractUserId(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return tokenService.getUserIdFromToken(authorizationHeader.substring(7));
        }
        return null;
    }
}