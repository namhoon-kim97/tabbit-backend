package com.jungle.Tabbit.domain.stampBadge.dto;

import com.jungle.Tabbit.domain.stampBadge.entity.Badge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "칭호를 가진 유저 응답 리스트 DTO")
public class UserWithBadgeResponseListDto {
    @Schema(description = "칭호 ID", example = "1")
    private Long badgeId;

    @Schema(description = "칭호 이름", example = "Super User")
    private String badgeName;

    @Schema(description = "칭호를 가진 유저 리스트")
    private List<MemberBadgeResponseDto> members;

    public static UserWithBadgeResponseListDto of(Badge badge, List<MemberBadgeResponseDto> members) {
        return UserWithBadgeResponseListDto.builder()
                .badgeId(badge.getBadgeId())
                .badgeName(badge.getName())
                .members(members)
                .build();
    }
}
