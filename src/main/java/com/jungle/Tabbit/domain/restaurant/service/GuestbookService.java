package com.jungle.Tabbit.domain.restaurant.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.restaurant.dto.guestbook.GuestbookRequestDto;
import com.jungle.Tabbit.domain.restaurant.dto.guestbook.GuestbookResponseDto;
import com.jungle.Tabbit.domain.restaurant.dto.guestbook.GuestbookResponseListDto;
import com.jungle.Tabbit.domain.restaurant.entity.Guestbook;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.repository.GuestbookRepository;
import com.jungle.Tabbit.domain.restaurant.repository.RestaurantRepository;
import com.jungle.Tabbit.domain.stampBadge.entity.MemberStamp;
import com.jungle.Tabbit.domain.stampBadge.repository.StampRepository;
import com.jungle.Tabbit.global.exception.BusinessLogicException;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GuestbookService {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final GuestbookRepository guestbookRepository;
    private final StampRepository stampRepository;

    @Transactional(readOnly = true)
    public GuestbookResponseListDto getAllGuestbook(String username, Long restaurantId, Long pageNumber, Long pageSize) {
        Member member = getMemberByUsername(username);
        Restaurant restaurant = getRestaurantById(restaurantId);

        List<Guestbook> guestbookList = restaurant.getGuestbookList();
        List<GuestbookResponseDto> guestbookResponseList = guestbookList.stream()
                .filter(guestbook -> (pageNumber - 1) * pageSize < guestbook.getMappingId() && guestbook.getMappingId() < pageNumber * pageSize)
                .map(GuestbookResponseDto::of)
                .collect(Collectors.toList());

        Boolean isWritable = stampRepository.findByMemberAndRestaurant(member, restaurant)
                .map(MemberStamp::isRecent)
                .orElse(false);

        return GuestbookResponseListDto.of(guestbookResponseList, isWritable);
    }

    public void createGuestbook(String username, Long restaurantId, GuestbookRequestDto requestDto) {
        Member member = getMemberByUsername(username);
        Restaurant restaurant = getRestaurantById(restaurantId);

        guestbookRepository.findByRestaurantAndMappingId(restaurant, requestDto.getMappingId())
                .ifPresent(guestbook -> {
                    throw new BusinessLogicException(ResponseStatus.FAIL_GUESTBOOK_MAPPING_ID);
                });

        Guestbook guestbook = new Guestbook(
                member,
                restaurant,
                requestDto.getContent(),
                requestDto.getMappingId()
        );
        guestbookRepository.save(guestbook);
    }

    public void deleteGuestbook(String username, Long restaurantId, Long guestbookId) {
        Member member = getMemberByUsername(username);
        Restaurant restaurant = getRestaurantById(restaurantId);
        if (!restaurant.getMember().equals(member)) {
            throw new BusinessLogicException(ResponseStatus.FAIL_NOT_OWNER);
        }

        Guestbook guestbook = getGuestbookById(guestbookId);
        guestbookRepository.delete(guestbook);
    }

    private Member getMemberByUsername(String username) {
        return memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
    }

    private Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_RESTAURANT_NOT_FOUND));
    }

    private Guestbook getGuestbookById(Long guestbookId) {
        return guestbookRepository.findByGuestbookId(guestbookId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_GUESTBOOK_NOT_FOUND));
    }
}
