package com.jungle.Tabbit.domain.stampBadge.service.condition;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.stampBadge.entity.BadgeTrigger;
import com.jungle.Tabbit.domain.stampBadge.service.TriggerType;

public interface TriggerCondition {
    boolean checkCondition(Member member, BadgeTrigger trigger);

    TriggerType getTriggerType();
}
