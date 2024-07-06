package com.jungle.Tabbit.domain.order.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.order.dto.MenuQuantityDto;
import com.jungle.Tabbit.domain.order.dto.OrderRequestDto;
import com.jungle.Tabbit.domain.order.dto.OrderResponseDto;
import com.jungle.Tabbit.domain.order.entity.Menu;
import com.jungle.Tabbit.domain.order.entity.Order;
import com.jungle.Tabbit.domain.order.entity.OrderMenu;
import com.jungle.Tabbit.domain.order.repository.MenuRepository;
import com.jungle.Tabbit.domain.order.repository.OrderRepository;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.repository.RestaurantRepository;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public OrderResponseDto createOrder(String username, OrderRequestDto requestDto) {
        Member member = getMemberByUsername(username);
        Restaurant restaurant = getRestaurantById(requestDto.getRestaurantId());

        Order order = new Order(member, restaurant);

        for (MenuQuantityDto menuQuantity : requestDto.getMenuQuantities()) {
            Menu menu = menuRepository.findByMenuId(menuQuantity.getMenuId())
                    .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MENU_NOT_FOUND));
            OrderMenu orderMenu = new OrderMenu(order, menu, menuQuantity.getQuantity());
            order.getOrderMenus().add(orderMenu);
        }

        orderRepository.save(order);
        return OrderResponseDto.of(order);
    }

    private Member getMemberByUsername(String username) {
        return memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
    }

    private Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_RESTAURANT_NOT_FOUND));
    }
}
