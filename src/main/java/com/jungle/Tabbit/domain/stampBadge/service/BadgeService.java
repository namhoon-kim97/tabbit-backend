package com.jungle.Tabbit.domain.stampBadge.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.stampBadge.dto.BadgeResponseDto;
import com.jungle.Tabbit.domain.stampBadge.dto.BadgeResponseListDto;
import com.jungle.Tabbit.domain.stampBadge.entity.Badge;
import com.jungle.Tabbit.domain.stampBadge.repository.BadgeRepository;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BadgeService {

    private final MemberRepository memberRepository;
    private final BadgeRepository badgeRepository;

    @Transactional(readOnly = true)
    public BadgeResponseListDto getBadgeAll(String username) {
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        List<Badge> badgeList = badgeRepository.findAll();

        Set<Long> badgedIds = member.getMemberBadgeList().stream()
                .map(badge -> badge.getBadge().getBadgeId())
                .collect(Collectors.toSet());

        List<BadgeResponseDto> badgeResponseList = badgeList.stream()
                .map(badge -> BadgeResponseDto.of(badge,
                        badgedIds.contains(badge.getBadgeId())))
                .collect(Collectors.toList());

        return BadgeResponseListDto.builder().badgeResponseList(badgeResponseList).build();
    }
}
