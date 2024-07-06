package com.jungle.Tabbit.domain.order.controller;

import com.jungle.Tabbit.domain.order.dto.menu.MenuCategoryRequestDto;
import com.jungle.Tabbit.domain.order.dto.menu.MenuCategoryResponseListDto;
import com.jungle.Tabbit.domain.order.dto.menu.MenuRequestDto;
import com.jungle.Tabbit.domain.order.dto.menu.MenuResponseListDto;
import com.jungle.Tabbit.domain.order.service.MenuService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
@Tag(name = "Menu API", description = "메뉴 관련 API")
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/category/{restaurantId}")
    @Operation(summary = "카테고리 조회", description = "해당 맛집의 카테고리를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MenuCategoryResponseListDto.class)))
    public CommonResponse<?> getMenuCategory(@PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId) {
        MenuCategoryResponseListDto responseDto = menuService.getAllMenuCategory(restaurantId);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @PostMapping("/category/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> createMenuCategory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId,
                                                @RequestBody @Parameter(description = "카테고리 생성 요청 DTO", required = true) MenuCategoryRequestDto requestDto) {
        menuService.createMenuCategory(userDetails.getUsername(), restaurantId, requestDto);
        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE);
    }

    @GetMapping("/{restaurantId}")
    @Operation(summary = "메뉴 조회", description = "해당 맛집의 메뉴를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MenuResponseListDto.class)))
    public CommonResponse<?> getMenu(@PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId) {
        MenuResponseListDto responseDto = menuService.getAllMenu(restaurantId);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, responseDto);
    }

    @PostMapping(value = "/{restaurantId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "메뉴 생성", description = "새로운 카테고리를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> createMenu(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId,
                                        @Parameter(description = "메뉴 생성 요청 DTO", required = true) MenuRequestDto requestDto) {
        menuService.createMenu(userDetails.getUsername(), restaurantId, requestDto);
        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE);
    }
}
