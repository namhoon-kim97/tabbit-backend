package com.jungle.Tabbit.domain.stampBadge.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.stampBadge.dto.BadgeResponseDto;
import com.jungle.Tabbit.domain.stampBadge.dto.BadgeResponseListDto;
import com.jungle.Tabbit.domain.stampBadge.dto.MemberBadgeResponseDto;
import com.jungle.Tabbit.domain.stampBadge.dto.UserWithBadgeResponseListDto;
import com.jungle.Tabbit.domain.stampBadge.entity.Badge;
import com.jungle.Tabbit.domain.stampBadge.entity.MemberBadge;
import com.jungle.Tabbit.domain.stampBadge.repository.BadgeRepository;
import com.jungle.Tabbit.domain.stampBadge.repository.MemberBadgeRepository;
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
    private final MemberBadgeRepository memberBadgeRepository;


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

        Long totalBadgeCount = (long) badgeList.size();
        Long earnedBadgeCount = (long) badgedIds.size();

        return BadgeResponseListDto.of(totalBadgeCount, earnedBadgeCount, badgeResponseList);
    }

    @Transactional(readOnly = true)
    public UserWithBadgeResponseListDto getUsersWithBadge(Long badgeId) {
        Badge badge = badgeRepository.findByBadgeId(badgeId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_BADGE_NOT_FOUND));

        List<MemberBadge> memberBadges = memberBadgeRepository.findByBadge_BadgeId(badgeId);

        List<MemberBadgeResponseDto> members = memberBadges.stream()
                .map(memberBadge -> MemberBadgeResponseDto.of(
                        memberBadge.getMember().getMemberId(),
                        memberBadge.getMember().getNickname(),
                        memberBadge.getBadge().getName()))
                .collect(Collectors.toList());

        return UserWithBadgeResponseListDto.of(badge, members);
    }

    public void awardBadge(Member member, Long badgeId) {
        Badge badge = badgeRepository.findByBadgeId(badgeId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_BADGE_NOT_FOUND));

        if (!memberBadgeRepository.existsByMemberAndBadge(member, badge)) {
            MemberBadge memberBadge = new MemberBadge(member, badge);
            memberBadgeRepository.save(memberBadge);
        }
    }
}
