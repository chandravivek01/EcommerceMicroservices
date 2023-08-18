package com.vcs.orderservice.service;

import com.vcs.orderservice.dto.InventoryResponse;
import com.vcs.orderservice.dto.OrderRequest;
import com.vcs.orderservice.dto.OrdersLineItemsDto;
import com.vcs.orderservice.model.Orders;
import com.vcs.orderservice.model.OrdersLineItems;
import com.vcs.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;
    public void placeOrder(OrderRequest orderRequest) {

        Orders orders = new Orders();
        orders.setOrderNumber(UUID.randomUUID().toString());

        List<OrdersLineItems> ordersLineItems = orderRequest.getOrdersLineItemsDto()
                .stream()
                .map(this::mapToDto)
                .toList();

        orders.setOrdersLineItemsList(ordersLineItems);

        List<String> codes = orders.getOrdersLineItemsList().stream().map(OrdersLineItems::getCode).toList();

        // Call inventory-service and check whether the product is in stock or not
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                        .uri("http://inventory-service/api/inventory",
                                uriBuilder -> uriBuilder.queryParam("code", codes).build())
                        .retrieve()
                        .bodyToMono(InventoryResponse[].class)
                        .block();
        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

        if(allProductsInStock)
            orderRepository.save(orders);
        else
            throw new IllegalArgumentException("Currently the product is not in the stock ");
    }

    private OrdersLineItems mapToDto(OrdersLineItemsDto ordersLineItemsDto) {

        System.out.println("inside mapToDto");
        OrdersLineItems ordersLineItems = new OrdersLineItems();
        ordersLineItems.setPrice(ordersLineItemsDto.getPrice());
        ordersLineItems.setQuantity(ordersLineItemsDto.getQuantity());
        ordersLineItems.setCode((ordersLineItemsDto.getCode()));
        return ordersLineItems;
    }
}
