package com.jungle.Tabbit.domain.order.repository;

import com.jungle.Tabbit.domain.order.entity.OrderMenu;
import com.jungle.Tabbit.domain.waiting.entity.Waiting;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface OrderMenuRepository extends Repository<OrderMenu, Long> {
    void save(OrderMenu orderMenu);

    List<OrderMenu> findByOrder_Waiting(Waiting waiting);

    void delete(OrderMenu orderMenu);
}
