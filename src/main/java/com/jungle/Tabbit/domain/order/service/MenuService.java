package com.jungle.Tabbit.domain.order.service;

import com.jungle.Tabbit.domain.image.service.ImageService;
import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.order.dto.menu.*;
import com.jungle.Tabbit.domain.order.entity.Menu;
import com.jungle.Tabbit.domain.order.entity.MenuCategory;
import com.jungle.Tabbit.domain.order.repository.MenuCategoryRepository;
import com.jungle.Tabbit.domain.order.repository.MenuRepository;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.repository.RestaurantRepository;
import com.jungle.Tabbit.global.exception.BusinessLogicException;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuRepository menuRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    public MenuCategoryResponseListDto getAllMenuCategory(Long restaurantId) {
        List<MenuCategory> MenuCategoryList = menuCategoryRepository.findAllByRestaurant_RestaurantId(restaurantId);

        List<MenuCategoryResponseDto> MenuCategoryDtoList = MenuCategoryList.stream()
                .map(MenuCategoryResponseDto::of)
                .toList();

        return MenuCategoryResponseListDto.of(MenuCategoryDtoList);
    }

    public void createMenuCategory(String username, Long restaurantId, MenuCategoryRequestDto requestDto) {
        Member member = getMemberByUsername(username);
        Restaurant restaurant = getRestaurantById(restaurantId);
        if (!restaurant.getMember().equals(member)) {
            throw new BusinessLogicException(ResponseStatus.FAIL_NOT_OWNER);
        }

        if (menuCategoryRepository.existsByRestaurant_RestaurantIdAndCategoryName(restaurantId, requestDto.getCategoryName())) {
            throw new BusinessLogicException(ResponseStatus.FAIL_CATEGORY_CREATE);
        }

        MenuCategory menuCategory = new MenuCategory(
                requestDto.getCategoryName(),
                restaurant
        );
        menuCategoryRepository.save(menuCategory);
    }

    public void updateMenuCategory(String username, Long menuCategoryId, MenuCategoryRequestDto requestDto) {
        Member member = getMemberByUsername(username);
        MenuCategory category = getMenuCategoryById(menuCategoryId);
        if (!category.getRestaurant().getMember().equals(member)) {
            throw new BusinessLogicException(ResponseStatus.FAIL_NOT_OWNER);
        }

        category.update(requestDto.getCategoryName());
    }

    public void deleteMenuCategory(String username, Long menuCategoryId) {
        Member member = getMemberByUsername(username);
        MenuCategory category = getMenuCategoryById(menuCategoryId);
        if (!category.getRestaurant().getMember().equals(member)) {
            throw new BusinessLogicException(ResponseStatus.FAIL_NOT_OWNER);
        }

        if (!category.getMenuList().isEmpty()) {
            throw new BusinessLogicException(ResponseStatus.FAIL_CATEGORY_DELETE);
        }

        menuCategoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public MenuResponseListDto getAllMenu(Long restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        List<Menu> menuList = menuRepository.findAllByRestaurantAndDelYn(restaurant, "N");
        List<MenuCategory> menuCategoryList = menuCategoryRepository.findAllByRestaurant_RestaurantId(restaurantId);

        List<String> menuCategoryDtoList = menuCategoryList.stream()
                .map(MenuCategory::getCategoryName)
                .toList();

        List<MenuResponseDto> menuDtoList = menuList.stream()
                .map(MenuResponseDto::of)
                .toList();

        return MenuResponseListDto.of(restaurant.getName(), menuCategoryDtoList, menuDtoList);
    }

    public void createMenu(String username, Long restaurantId, MenuRequestDto requestDto) {
        Member member = getMemberByUsername(username);
        Restaurant restaurant = getRestaurantById(restaurantId);
        MenuCategory category = getMenuCategoryById(requestDto.getCategoryId());
        if (!restaurant.getMember().equals(member)) {
            throw new BusinessLogicException(ResponseStatus.FAIL_NOT_OWNER);
        }

        String imageFileName = imageService.uploadImage(requestDto.getMultipartFile());

        Menu menu = new Menu(
                restaurant,
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getDescription(),
                imageFileName,
                category
        );
        menuRepository.save(menu);
    }

    public void updateMenu(String username, Long menuId, MenuRequestDto requestDto) {
        Member member = getMemberByUsername(username);
        Menu menu = getMenuById(menuId);
        MenuCategory category = getMenuCategoryById(requestDto.getCategoryId());
        if (!menu.getRestaurant().getMember().equals(member)) {
            throw new BusinessLogicException(ResponseStatus.FAIL_NOT_OWNER);
        }

        MultipartFile file = requestDto.getMultipartFile();
        if (imageService.isExistImage(file.getOriginalFilename())) {
            imageService.deleteImage(file.getOriginalFilename());
        }

        String imageFileName = imageService.uploadImage(file);

        menu.update(requestDto.getName(), requestDto.getPrice(), requestDto.getDescription(), imageFileName, category);
    }

    public void deleteMenu(String username, Long menuId) {
        Member member = getMemberByUsername(username);
        Menu menu = getMenuById(menuId);
        if (!menu.getRestaurant().getMember().equals(member)) {
            throw new BusinessLogicException(ResponseStatus.FAIL_NOT_OWNER);
        }

        menu.delete();
    }

    private Member getMemberByUsername(String username) {
        return memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
    }

    private Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_RESTAURANT_NOT_FOUND));
    }

    private MenuCategory getMenuCategoryById(Long categoryId) {
        return menuCategoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CATEGORY_NOT_FOUND));
    }

    private Menu getMenuById(Long menuId) {
        return menuRepository.findByMenuId(menuId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MENU_NOT_FOUND));
    }
}
