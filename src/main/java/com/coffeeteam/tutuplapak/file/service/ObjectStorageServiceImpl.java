package com.coffeeteam.tutuplapak.file.service;

import com.coffeeteam.tutuplapak.file.dto.UploadResultDto;
import com.coffeeteam.tutuplapak.minio.MinioProperties;
import io.minio.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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

    private String constructObjectName(MultipartFile file) {
        String[] split = file.getName().split("\\.");
        String extension = "";

        if ( split.length == 1 ) {
            extension = "jpg";
        } else {
            extension = split[ split.length - 1 ];
        }

        return String.format("%s-%s.%s", file.getName(), UUID.randomUUID(), extension);
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
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to prepare bucket: " + BUCKET_NAME);
        }
    }

    @Override
    public UploadResultDto upload(MultipartFile file) {
        ensureBucket();

        try (InputStream is = file.getInputStream()) {
            String objectname = constructObjectName(file);
            PutObjectArgs argument = PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .stream(is, file.getSize(), -1)
                    .region("us-east")
                    .object(objectname)
                    .contentType(file.getContentType())
                    .build();

            ObjectWriteResponse response = minioClient.putObject(argument);

            return UploadResultDto
                    .builder()
                    .bucket(BUCKET_NAME)
                    .objectName(objectname)
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .etag(response.etag())
                    .presignedUrl("")
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("System failed to upload the file", e);
        }
    }
}
