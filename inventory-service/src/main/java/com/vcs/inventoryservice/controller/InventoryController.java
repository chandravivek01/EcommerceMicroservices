package com.vcs.inventoryservice.controller;

import com.vcs.inventoryservice.dto.InventoryResponse;
import com.vcs.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // http://localhost:8082/api/inventory?code=iphone-12&code=iphone-12-red
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> code) {

        return inventoryService.isInStock(code);
    }
}
