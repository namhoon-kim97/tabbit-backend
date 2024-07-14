package com.jungle.Tabbit.domain.stampBadge.service.condition;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.stampBadge.entity.BadgeTrigger;
import com.jungle.Tabbit.domain.stampBadge.service.TriggerType;
import org.springframework.stereotype.Component;

@Component
public class TotalStampsCondition implements TriggerCondition {
    @Override
    public boolean checkCondition(Member member, BadgeTrigger trigger) {
        // 전체 도장 개수를 기준으로 배지를 수여
        return member.getMemberStampList().size() >= Long.parseLong(trigger.getCondition());
    }

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.TOTAL_STAMPS;
    }
}