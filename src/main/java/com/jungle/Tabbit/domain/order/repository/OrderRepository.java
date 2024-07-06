package com.jungle.Tabbit.domain.order.repository;

import com.jungle.Tabbit.domain.order.entity.Order;
import org.springframework.data.repository.Repository;

public interface OrderRepository extends Repository<Order, Long> {
    void save(Order order);
}
