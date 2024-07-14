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
    @Operation(summary = "카테고리 생성", description = "해당 맛집의 새로운 카테고리를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> createMenuCategory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @PathVariable @Parameter(description = "맛집 ID", required = true) Long restaurantId,
                                                @RequestBody @Parameter(description = "카테고리 생성 요청 DTO", required = true) MenuCategoryRequestDto requestDto) {
        menuService.createMenuCategory(userDetails.getUsername(), restaurantId, requestDto);
        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE);
    }

    @PutMapping("/category/{menuCategoryId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "카테고리 수정", description = "해당 카테고리를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> updateMenuCategory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @PathVariable @Parameter(description = "카테고리 ID", required = true) Long menuCategoryId,
                                                @RequestBody @Parameter(description = "카테고리 수정 요청 DTO", required = true) MenuCategoryRequestDto requestDto) {
        menuService.updateMenuCategory(userDetails.getUsername(), menuCategoryId, requestDto);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK);
    }

    @DeleteMapping("/category/{menuCategoryId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "카테고리 삭제", description = "해당 카테고리를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> deleteMenuCategory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @PathVariable @Parameter(description = "카테고리 ID", required = true) Long menuCategoryId) {
        menuService.deleteMenuCategory(userDetails.getUsername(), menuCategoryId);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK);
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

    @PutMapping(value = "/{menuId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "메뉴 수정", description = "해당 메뉴를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> updateMenu(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable @Parameter(description = "메뉴 ID", required = true) Long menuId,
                                        @Parameter(description = "메뉴 수정 요청 DTO", required = true) MenuRequestDto requestDto) {
        menuService.updateMenu(userDetails.getUsername(), menuId, requestDto);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK);
    }

    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "메뉴 삭제", description = "해당 메뉴를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> deleteMenu(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable @Parameter(description = "메뉴 ID", required = true) Long menuId) {
        menuService.deleteMenu(userDetails.getUsername(), menuId);
        return CommonResponse.success(ResponseStatus.SUCCESS_OK);
    }
}
