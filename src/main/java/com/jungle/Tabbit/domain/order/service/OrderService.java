package com.jungle.Tabbit.domain.order.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.order.dto.order.MenuQuantityDto;
import com.jungle.Tabbit.domain.order.dto.order.OrderRequestDto;
import com.jungle.Tabbit.domain.order.dto.order.OrderResponseDto;
import com.jungle.Tabbit.domain.order.dto.order.OrderUpdateRequestDto;
import com.jungle.Tabbit.domain.order.entity.Menu;
import com.jungle.Tabbit.domain.order.entity.Order;
import com.jungle.Tabbit.domain.order.entity.OrderMenu;
import com.jungle.Tabbit.domain.order.entity.OrderStatus;
import com.jungle.Tabbit.domain.order.repository.MenuRepository;
import com.jungle.Tabbit.domain.order.repository.OrderMenuRepository;
import com.jungle.Tabbit.domain.order.repository.OrderRepository;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.repository.RestaurantRepository;
import com.jungle.Tabbit.domain.waiting.entity.Waiting;
import com.jungle.Tabbit.domain.waiting.entity.WaitingStatus;
import com.jungle.Tabbit.domain.waiting.repository.WaitingRepository;
import com.jungle.Tabbit.global.exception.BusinessLogicException;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final WaitingRepository waitingRepository;

    @Transactional
    public void createOrder(String username, OrderRequestDto requestDto) {
        Member member = getMemberByUsername(username);
        Restaurant restaurant = getRestaurantById(requestDto.getRestaurantId());
        Waiting waiting = waitingRepository.findByRestaurantAndWaitingNumberAndWaitingStatus(restaurant, requestDto.getWaitingNumber(), WaitingStatus.STATUS_WAITING)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_GET_CURRENT_WAIT_POSITION));

        Order order = new Order(member, restaurant, waiting);
        orderRepository.save(order);
        for (MenuQuantityDto menuQuantity : requestDto.getMenuQuantities()) {
            Menu menu = menuRepository.findByMenuId(menuQuantity.getMenuId())
                    .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MENU_NOT_FOUND));
            OrderMenu orderMenu = new OrderMenu(order, menu, menuQuantity.getQuantity());
            orderMenuRepository.save(orderMenu);
        }
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getUserOrders(Long waitingId) {
        Order order = orderRepository.findByWaiting_WaitingId(waitingId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_ORDER_NOT_FOUND));
        return OrderResponseDto.of(order);
    }

    @Transactional
    public void updateOrderStatusToConfirmed(Member member, Restaurant restaurant) {
        orderRepository.findByMemberAndRestaurantAndStatus(member, restaurant, OrderStatus.ORDERED)
                .ifPresent(order -> order.updateStatus(OrderStatus.CONFIRMED));
    }

    @Transactional
    public void deleteOrderIfExists(Long waitingId) {
        orderRepository.findByWaiting_WaitingId(waitingId)
                .ifPresent(orderRepository::delete);
    }

    @Transactional
    public void updateOrder(String username, OrderUpdateRequestDto requestDto, Long orderId) {
        Member member = getMemberByUsername(username);
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_ORDER_NOT_FOUND));

        if (!order.getMember().equals(member)) {
            throw new BusinessLogicException(ResponseStatus.FAIL_ORDER_MEMBER_EQUAL);
        }

        Map<Long, MenuQuantityDto> menuQuantityMap = requestDto.getMenuQuantities().stream()
                .collect(Collectors.toMap(MenuQuantityDto::getMenuId, mq -> mq));

        // 기존 주문 메뉴를 순회하면서 업데이트 또는 삭제 처리
        order.getOrderMenus().removeIf(orderMenu -> {
            MenuQuantityDto menuQuantityDto = menuQuantityMap.remove(orderMenu.getMenu().getMenuId());
            if (menuQuantityDto != null) {
                orderMenu.updateQuantity(menuQuantityDto.getQuantity());
                return false; // 삭제하지 않음
            } else {
                orderMenuRepository.delete(orderMenu); // 삭제
                return true; // 리스트에서 제거
            }
        });

        // 남아있는 요청 메뉴들을 새로운 주문 메뉴로 추가
        menuQuantityMap.values().forEach(menuQuantityDto -> {
            Menu menu = menuRepository.findByMenuId(menuQuantityDto.getMenuId())
                    .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MENU_NOT_FOUND));
            OrderMenu newOrderMenu = new OrderMenu(order, menu, menuQuantityDto.getQuantity());
            orderMenuRepository.save(newOrderMenu);
        });
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
