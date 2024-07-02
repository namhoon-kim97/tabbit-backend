package com.jungle.Tabbit.domain.stampBadge.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.stampBadge.entity.BadgeTrigger;
import com.jungle.Tabbit.domain.stampBadge.entity.MemberStamp;
import com.jungle.Tabbit.domain.stampBadge.repository.StampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LocationCondition implements TriggerCondition {
    private final StampRepository stampRepository;

    @Override
    public boolean checkCondition(Member member, BadgeTrigger trigger) {
        List<MemberStamp> stamps = stampRepository.findByMember(member);
        long count = stamps.stream()
                .filter(stamp -> stamp.getRestaurant().getAddress().getSido().equals(trigger.getCondition()))
                .count();
        return count >= trigger.getRequiredStampCnt();
    }

    @Override
    public TriggerType getTriggerType() {
        return TriggerType.LOCATION;
    }
}
