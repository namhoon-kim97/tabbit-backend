package com.jungle.Tabbit.domain.order.repository;

import com.jungle.Tabbit.domain.order.entity.OrderMenu;
import org.springframework.data.repository.Repository;

public interface OrderMenuRepository extends Repository<OrderMenu, Long> {
    void save(OrderMenu orderMenu);
}
