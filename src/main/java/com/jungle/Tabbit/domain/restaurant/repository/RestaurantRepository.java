package com.jungle.Tabbit.domain.restaurant.repository;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends Repository<Restaurant, Long> {
    List<Restaurant> findAll();

    void save(Restaurant restaurant);

    @EntityGraph(attributePaths = {"category", "address"})
    Optional<Restaurant> findByRestaurantId(Long id);

    List<Restaurant> findAllByMember(Member member);

    @Query("SELECT r FROM restaurant r " +
            "JOIN r.category c " +
            "JOIN r.address a " +
            "WHERE r.name LIKE %:searchTerm% " +
            "OR c.categoryName LIKE %:searchTerm% " +
            "OR a.sido LIKE %:searchTerm% " +
            "OR a.sigungu LIKE %:searchTerm% " +
            "OR a.eupmyeondong LIKE %:searchTerm% " +
            "OR a.roadAddress LIKE %:searchTerm% " +
            "OR a.streetAddress LIKE %:searchTerm% "
            )
    List<Restaurant> searchRestaurants(@Param("searchTerm") String searchTerm);
}
