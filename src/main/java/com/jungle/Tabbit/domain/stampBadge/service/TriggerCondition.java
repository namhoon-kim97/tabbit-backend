package com.jungle.Tabbit.domain.stampBadge.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.stampBadge.entity.BadgeTrigger;

public interface TriggerCondition {
    boolean checkCondition(Member member, BadgeTrigger trigger);

    TriggerType getTriggerType();
}
