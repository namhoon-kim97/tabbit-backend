package com.jungle.Tabbit.domain.order.repository;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.order.entity.Order;
import com.jungle.Tabbit.domain.order.entity.OrderStatus;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface OrderRepository extends Repository<Order, Long> {
    void save(Order order);

    Optional<Order> findByMemberAndRestaurantAndStatus(Member member, Restaurant restaurant, OrderStatus orderStatus);
}
