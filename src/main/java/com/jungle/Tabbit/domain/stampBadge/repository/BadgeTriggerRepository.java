package com.jungle.Tabbit.domain.stampBadge.repository;

import com.jungle.Tabbit.domain.stampBadge.entity.BadgeTrigger;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface BadgeTriggerRepository extends Repository<BadgeTrigger, Long> {
    void save(BadgeTrigger badgeTrigger);

    @Query("SELECT bt FROM BadgeTrigger bt WHERE bt.category.categoryCd IN :categoryCds OR bt.category IS NULL")
    List<BadgeTrigger> findByCategoryCdsOrCategoryIsNull(List<String> categoryCds);

    @Query("SELECT bt FROM BadgeTrigger bt WHERE bt.triggerType = 'TOTAL_STAMPS' OR bt.triggerType = 'DIVERSITY'")
    List<BadgeTrigger> findTotalStampsAndDiversityTriggers();

    List<BadgeTrigger> findByTriggerTypeIn(List<String> triggerTypes);
}
