package com.jungle.Tabbit.domain.stampBadge.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.stampBadge.entity.BadgeTrigger;
import com.jungle.Tabbit.domain.stampBadge.entity.MemberStamp;
import org.springframework.stereotype.Component;

@Component
public class CategoryStampsCondition implements TriggerCondition {
    @Override
    public boolean checkCondition(Member member, BadgeTrigger trigger) {
        return member.getMemberStampList().stream()
                .filter(stamp -> stamp.getCategory().getCategoryCd().equals(trigger.getCategory().getCategoryCd()))
                .mapToLong(MemberStamp::getVisitCount)
                .sum() >= Long.parseLong(trigger.getCondition());
    }

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.CATEGORY_STAMPS;
    }
}
