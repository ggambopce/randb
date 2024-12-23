package com.jinho.randb.domain.image.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.jinho.randb.global.exception.ex.img.ImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import static com.jinho.randb.global.exception.ex.img.ImageErrorType.INVALID_IMAGE_FORMAT;

@Service
@Transactional
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile) throws IOException {

        // 원본 파일명과 저장될 파일명 생성
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFile = createStoreFile(originalFilename);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    /* 저장될 파일명 생성 */
    private String createStoreFile(String originalFilename) {
        int lastIndexOf = originalFilename.lastIndexOf(".");                    // 마지막 종류
        String extension = originalFilename.substring(lastIndexOf + 1).toLowerCase();     //확장자 종류

        Set<String> allowedExtensions = Set.of("jpeg", "jpg", "png");
        if (!allowedExtensions.contains(extension)) {
            throw new ImageException(INVALID_IMAGE_FORMAT);
        }

        String uuid = UUID.randomUUID().toString();
        return uuid+"."+extension;
    }
}

