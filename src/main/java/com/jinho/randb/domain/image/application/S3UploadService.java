package com.jinho.randb.domain.image.application;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jinho.randb.domain.image.domain.UploadFile;
import com.jinho.randb.global.exception.ex.img.ImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

import static com.jinho.randb.global.exception.ex.img.ImageErrorType.INVALID_IMAGE_FORMAT;
import static com.jinho.randb.global.exception.ex.img.ImageErrorType.UPLOAD_FAILS;

@Service
@Transactional
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile multipartFile){

        // 원본 파일명과 저장될 파일명 생성
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFile(originalFilename);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        // 파일을 S3에 업로드
        try{
            amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        }catch (IOException e) {
            e.printStackTrace();
            throw new ImageException(UPLOAD_FAILS);
        }

        return amazonS3.getUrl(bucket, storeFilename).toString();
    }

    /**
     * S3 이미지를 수정하는 메서드
     * AWS S3는 덮어쓰는방식은 지원되지 않으므로 삭제후 재 업로드
     */
    public void updateFile(String existingFileName, MultipartFile newFile) {

        // 기존 파일 삭제
        if (existingFileName != null && !existingFileName.isEmpty()) {
            deleteFile(existingFileName);
        }
        // 새로운 파일 업로드
        uploadFile(newFile);
    }

    /**
     * S3 이미지를 삭제하는 메서드
     * 저장된 파일명을 사용해 S3 버킷에서 해당 이미지를 삭제
     */
    public void deleteFile(String uploadFileName){
        try{
            amazonS3.deleteObject(bucket, uploadFileName);
        }catch (SdkClientException e){
            throw new ImageException(UPLOAD_FAILS);
        }
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

