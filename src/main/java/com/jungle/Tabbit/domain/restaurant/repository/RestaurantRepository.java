package com.jungle.Tabbit.domain.restaurant.repository;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends Repository<Restaurant, Long> {
    List<Restaurant> findAll();

    @EntityGraph(attributePaths = {"address"})
    @Query("SELECT r FROM Restaurant r")
    List<Restaurant> findAllWithAddress();

    void save(Restaurant restaurant);

    @EntityGraph(attributePaths = {"category", "address", "restaurantDetail"})
    Optional<Restaurant> findByRestaurantId(Long id);

    List<Restaurant> findAllByMember(Member member);
}
