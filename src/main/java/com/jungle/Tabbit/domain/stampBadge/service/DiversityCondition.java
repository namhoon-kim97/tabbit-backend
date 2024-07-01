package com.jungle.Tabbit.domain.stampBadge.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.stampBadge.entity.BadgeTrigger;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DiversityCondition implements TriggerCondition {
    @Override
    public boolean checkCondition(Member member, BadgeTrigger trigger) {
        Set<String> categories = member.getMemberStampList().stream()
                .map(stamp -> stamp.getCategory().getCategoryCd())
                .collect(Collectors.toSet());
        return categories.size() >= Long.parseLong(trigger.getCondition());
    }

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.DIVERSITY;
    }
}
