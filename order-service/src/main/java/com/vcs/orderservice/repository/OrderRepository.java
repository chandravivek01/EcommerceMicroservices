package com.vcs.orderservice.repository;

import com.vcs.orderservice.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
