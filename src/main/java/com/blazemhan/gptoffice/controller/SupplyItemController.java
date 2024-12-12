package com.blazemhan.gptoffice.controller;

import com.blazemhan.gptoffice.entity.SupplyItem;
import com.blazemhan.gptoffice.service.SupplyItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class SupplyItemController {

    private final SupplyItemService itemService;

    public SupplyItemController(SupplyItemService itemService) {
        this.itemService = itemService;
    }

    // Add a New Supply Item
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addSupplyItem(@RequestBody SupplyItem item) {
        SupplyItem newItem = itemService.addSupplyItem(item);
        return ResponseEntity.ok("Item added: " + newItem.getName());
    }

    // Get All Supply Items
    @GetMapping("/list")
    public ResponseEntity<List<SupplyItem>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    // Get Supply Item by ID
    @GetMapping("/{id}")
    public ResponseEntity<SupplyItem> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }
}

