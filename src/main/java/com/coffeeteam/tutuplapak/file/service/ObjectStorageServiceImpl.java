package com.coffeeteam.tutuplapak.file.service;

import com.coffeeteam.tutuplapak.file.dto.UploadResultDto;
import com.coffeeteam.tutuplapak.minio.MinioProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ObjectStorageServiceImpl implements ObjectStorageService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    private final String BUCKET_NAME = "upload";

    public ObjectStorageServiceImpl(
            MinioClient minioClient,
            MinioProperties minioProperties
    ) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    private String constructObjectName(String filename) {
        String[] split = filename.split("\\.");
        String extension = "";

        if ( split.length == 1 ) {
            extension = "jpg";
        } else {
            extension = split[ split.length - 1 ];
        }

        return String.format("%s-%s.%s", filename, UUID.randomUUID(), extension);
    }

    private String getPublicBucketPolicyJson() {
        Map<String, Object> json = new HashMap<>();
        json.put("Version", "2012-10-17");
        json.put("Statement", List.of(
                Map.of(
                        "Action", "s3:GetObject",
                        "Effect", "Allow",
                        "Principal", "*",
                        "Resource", "arn:aws:s3:::" + BUCKET_NAME + "/*"
                )
        ));

        try {
            ObjectMapper om = new ObjectMapper();
            return om.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void ensureBucket() {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(BUCKET_NAME)
                    .build());

            if ( !bucketExists ) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(BUCKET_NAME)
                        .build());

                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(BUCKET_NAME)
                        .config(getPublicBucketPolicyJson())
                        .build());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to prepare bucket: " + BUCKET_NAME, e);
        }
    }

    @Override
    public UploadResultDto upload(InputStream inputStream, String filename, long size, String contentType) {
        ensureBucket();

        try {
            String objectname = constructObjectName(filename);
            PutObjectArgs argument = PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .stream(inputStream, size, -1)
                    .region("us-east")
                    .object(objectname)
                    .contentType(contentType)
                    .build();

            ObjectWriteResponse response = minioClient.putObject(argument);

            return UploadResultDto
                    .builder()
                    .bucket(BUCKET_NAME)
                    .objectName(objectname)
                    .contentType(contentType)
                    .size(size)
                    .etag(response.etag())
                    .presignedUrl("")
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("System failed to upload the file", e);
        }
    }
}
