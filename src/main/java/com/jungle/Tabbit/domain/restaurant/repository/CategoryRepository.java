package com.jungle.Tabbit.domain.restaurant.repository;

import com.jungle.Tabbit.domain.restaurant.entity.Category;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CategoryRepository extends Repository<Category, Long> {
    Optional<Category> findByCategoryCd(String categoryCd);
}
