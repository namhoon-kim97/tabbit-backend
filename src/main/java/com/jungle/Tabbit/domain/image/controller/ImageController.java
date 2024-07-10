package com.jungle.Tabbit.domain.image.controller;

import com.jungle.Tabbit.domain.image.service.ImageService;
import com.jungle.Tabbit.global.model.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
@Tag(name = "Image API", description = "이미지 관련 API")
public class ImageController {

    private final ImageService imageService;

    @GetMapping
    @Operation(summary = "이미지 조회", description = "이미지를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public ResponseEntity<byte[]> getImage(@RequestParam @Parameter(description = "요청 이미지 url", required = true) String imageUrl) {
        log.info("imageUrl: {}@@@@@@@@@@@@@@@@@@@@@@@", imageUrl);
        byte[] imageBytes = imageService.getImage(imageUrl);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageService.getContentType(imageUrl)))
                .body(imageBytes);
    }
}
