package com.vcs.inventoryservice.service;

import com.vcs.inventoryservice.dto.InventoryResponse;
import com.vcs.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> code) {

        return inventoryRepository.findByCodeIn(code)
                .stream()
                .map(inventory -> InventoryResponse.builder().code(inventory.getCode())
                        .isInStock(inventory.getQuantity() > 0)
                        .build()).toList();
    }
}
