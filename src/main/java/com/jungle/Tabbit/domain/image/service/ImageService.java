package com.jungle.Tabbit.domain.image.service;

import com.jungle.Tabbit.global.exception.FileException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.google.common.io.Files.getFileExtension;

@Slf4j
@Service
public class ImageService {

    @Value("${spring.file.upload.restaurant}")
    private String uploadFolder;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_FILE_SIZE;

    private static final List<String> imageMimeTypesList = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif",
            "image/bmp",
            "image/webp"
    );

    public String uploadImage(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String mimeType = file.getContentType();

        if (originalFileName.equals("DEFAULT")) {
            return originalFileName;
        }

        if (file.getSize() > parseFileSize(MAX_FILE_SIZE)) {
            throw new FileException(ResponseStatus.FAIL_FILE_SIZE);
        }

        if (!isImageFile(mimeType)) {
            throw new FileException(ResponseStatus.FAIL_FILE_MIME);
        }

        createUploadFolderIfNeeded(uploadFolder);

        String imageFileName = getUniqueFileName(originalFileName);
        Path filePath = Paths.get(uploadFolder, imageFileName);

        try {
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            log.error("파일을 저장하는 도중 오류가 발생했습니다. 파일명 : {}", originalFileName, e);
            throw new FileException(ResponseStatus.FAIL_FILE_UPLOAD);
        }

        return imageFileName;
    }

    public byte[] getImage(String imageUrl) {
        // 프로필 이미지 경로가 null이 아니고 빈 문자열이 아닌 경우
        if (!StringUtils.isEmpty(imageUrl)) {
            Path imagePath = Paths.get(uploadFolder, imageUrl);
            // 파일이 존재하는 경우
            if (Files.exists(imagePath)) {
                try {
                    return Files.readAllBytes(imagePath);
                } catch (IOException e) {
                    log.error("파일을 불러오는 도중 오류가 발생했습니다. 파일명 : {}", imagePath, e);
                    throw new FileException(ResponseStatus.FAIL_FILE_LOAD);
                }
            } else {
                throw new FileException(ResponseStatus.FAIL_FILE_NOT_FOUND);
            }
        } else {
            throw new FileException(ResponseStatus.FAIL_FILE_PATH);
        }
    }

    public String getContentType(String imageUrl) {
        String extension = getFileExtension(imageUrl).toLowerCase();
        for (String mimeType : imageMimeTypesList) {
            if (mimeType.endsWith(extension)) {
                return mimeType;
            }
        }
        return null;
    }

    private Boolean isImageFile(String mimeType) {
        return imageMimeTypesList.contains(mimeType);
    }

    private String getUniqueFileName(String originalFileName) {
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + originalFileName;
        return imageFileName;
    }

    private void createUploadFolderIfNeeded(String uploadFolder) {
        Path uploadFolderPath = Paths.get(uploadFolder);
        if (!Files.exists(uploadFolderPath)) {
            try {
                Files.createDirectories(uploadFolderPath);
            } catch (IOException e) {
                log.error("업로드 폴더를 생성하는 도중 오류가 발생했습니다. 경로 : {}", uploadFolder, e);
                throw new FileException(ResponseStatus.FAIL_FILE_UPLOAD);
            }
        }
    }

    private long parseFileSize(String fileSizeString) {
        if (fileSizeString == null || fileSizeString.isEmpty()) {
            return 0L;
        }

        fileSizeString = fileSizeString.trim().toUpperCase();

        long multiplier = 1L;
        if (fileSizeString.endsWith("KB")) {
            multiplier = 1024L;
            fileSizeString = fileSizeString.substring(0, fileSizeString.length() - 2).trim();
        } else if (fileSizeString.endsWith("MB")) {
            multiplier = 1024L * 1024L;
            fileSizeString = fileSizeString.substring(0, fileSizeString.length() - 2).trim();
        } else if (fileSizeString.endsWith("GB")) {
            multiplier = 1024L * 1024L * 1024L;
            fileSizeString = fileSizeString.substring(0, fileSizeString.length() - 2).trim();
        } else if (fileSizeString.endsWith("TB")) {
            multiplier = 1024L * 1024L * 1024L * 1024L;
            fileSizeString = fileSizeString.substring(0, fileSizeString.length() - 2).trim();
        } else if (fileSizeString.endsWith("B")) {
            fileSizeString = fileSizeString.substring(0, fileSizeString.length() - 1).trim();
        }

        try {
            return Long.parseLong(fileSizeString) * multiplier;
        } catch (NumberFormatException ex) {
            return 0L;
        }
    }
}
