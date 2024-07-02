package com.jungle.Tabbit.domain.stampBadge.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.restaurant.entity.Category;
import com.jungle.Tabbit.domain.restaurant.repository.CategoryRepository;
import com.jungle.Tabbit.domain.stampBadge.dto.BadgeTriggerRequestDto;
import com.jungle.Tabbit.domain.stampBadge.entity.Badge;
import com.jungle.Tabbit.domain.stampBadge.entity.BadgeTrigger;
import com.jungle.Tabbit.domain.stampBadge.repository.BadgeRepository;
import com.jungle.Tabbit.domain.stampBadge.repository.BadgeTriggerRepository;
import com.jungle.Tabbit.domain.stampBadge.repository.MemberBadgeRepository;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BadgeTriggerService {
    private final BadgeTriggerRepository badgeTriggerRepository;
    private final BadgeRepository badgeRepository;
    private final CategoryRepository categoryRepository;
    private final BadgeService badgeService;
    private final List<TriggerCondition> conditions;
    private final MemberBadgeRepository memberBadgeRepository;

    public void checkAndAwardBadges(Member member) {
        // 회원의 모든 도장의 카테고리 코드를 가져옵니다.
        Set<String> categoryCds = member.getMemberStampList().stream()
                .map(stamp -> stamp.getCategory().getCategoryCd())
                .collect(Collectors.toSet());

        // 해당 카테고리 코드와 관련된 모든 트리거를 가져옵니다.
        List<BadgeTrigger> triggers = badgeTriggerRepository.findByCategoryCdsOrCategoryIsNull(new ArrayList<>(categoryCds));

        // 트리거를 처리합니다.
        manipulateTrigger(member, triggers);

        // TOTAL_STAMPS 및 DIVERSITY, LOCATION 트리거를 별도로 처리합니다.
        List<BadgeTrigger> specialTriggers = badgeTriggerRepository.findByTriggerTypeIn(List.of("TOTAL_STAMPS", "DIVERSITY", "LOCATION"));
        manipulateTrigger(member, specialTriggers);
    }

    private void manipulateTrigger(Member member, List<BadgeTrigger> triggers) {
        for (BadgeTrigger trigger : triggers) {
            if (!memberBadgeRepository.existsByMemberAndBadge(member, trigger.getBadge())) {
                Optional<TriggerCondition> condition = conditions.stream()
                        .filter(cond -> cond.getTriggerType().name().equalsIgnoreCase(trigger.getTriggerType()))
                        .findFirst();
                if (condition.isPresent() && condition.get().checkCondition(member, trigger)) {
                    badgeService.awardBadge(member, trigger.getBadge().getBadgeId());
                }
            }
        }
    }

    @Transactional
    public void addBadgeTrigger(BadgeTriggerRequestDto requestDto) {
        Badge badge = badgeRepository.findByBadgeId(requestDto.getBadgeId())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_BADGE_NOT_FOUND));
        Category category = categoryRepository.findByCategoryCd(requestDto.getCategoryCd())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CATEGORY_NOT_FOUND));

        BadgeTrigger badgeTrigger = new BadgeTrigger(badge, category, requestDto.getRequiredStampCnt(), requestDto.getTriggerType(), requestDto.getCondition());
        badgeTriggerRepository.save(badgeTrigger);
    }
}
