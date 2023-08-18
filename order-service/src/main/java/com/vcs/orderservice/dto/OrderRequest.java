package com.vcs.orderservice.dto;

import com.vcs.orderservice.model.OrdersLineItems;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private List<OrdersLineItemsDto> ordersLineItemsDto;

}
