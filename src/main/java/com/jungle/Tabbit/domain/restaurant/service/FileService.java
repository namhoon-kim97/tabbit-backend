package com.jungle.Tabbit.domain.restaurant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    //    @Value("${file.upload}")
    private String uploadDir = "/Users/gwonjihyeon/Downloads";

    public String storeFile(MultipartFile file) {
        try {
            String imageFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            System.out.println("커버 이미지 파일이름:" + imageFileName);
            Path imageFilePath = Paths.get(uploadDir, imageFileName);
            Files.write(imageFilePath, file.getBytes());
            return imageFileName;
        } catch (IOException e) {
            return null;
        }
    }
}
