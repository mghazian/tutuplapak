package com.coffeeteam.tutuplapak.file.service;

import com.coffeeteam.tutuplapak.auth.security.CustomUserDetails;
import com.coffeeteam.tutuplapak.file.dto.FileUploadResponseDto;
import com.coffeeteam.tutuplapak.file.dto.UploadResultDto;
import com.coffeeteam.tutuplapak.file.exception.FileInputInvalidException;
import com.coffeeteam.tutuplapak.file.model.Image;
import com.coffeeteam.tutuplapak.file.repository.ImageRepository;
import com.coffeeteam.tutuplapak.minio.MinioProperties;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final ObjectStorageService objectStorageService;
    private final ImageRepository imageRepository;
    private final Tika tika;
    private final MinioProperties minioProperties;

    public FileUploadServiceImpl(
            ObjectStorageService objectStorageService,
            ImageRepository imageRepository, MinioProperties minioProperties
    ) {
        this.objectStorageService = objectStorageService;
        this.imageRepository = imageRepository;
        this.minioProperties = minioProperties;
        this.tika = new Tika();
    }

    private byte[] compress(MultipartFile file) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Thumbnails.of(file.getInputStream())
                .size(100, 100)
                .outputQuality(0.25f)
                .toOutputStream(os);

        return os.toByteArray();
    }

    boolean isFileTypeAcceptable(MultipartFile file) {
        try {
            String extension = tika.detect(file.getInputStream());

            return List.of("image/jpg", "image/jpeg", "image/png").contains(extension);
        } catch (IOException e) {
            // TODO: Not good - should handle exception properly but acceptable for now
            return false;
        }
    }

    @Override
    public FileUploadResponseDto upload(MultipartFile file, CustomUserDetails userDetails) {
        // Validate file
        if ( ! isFileTypeAcceptable(file) ) {
            throw new FileInputInvalidException("File type is invalid");
        }

        if ( file.getSize() > 100_000 ) {
            throw new FileInputInvalidException("File size cannot be larger than 100KB");
        }

        UploadResultDto result = null;
        UploadResultDto compressedResult = null;
        try (InputStream is = file.getInputStream()) {
            result = objectStorageService.upload(is, file.getName(), file.getSize(), file.getContentType());

            byte[] byteArray = compress(file);
            InputStream compressedIs = new ByteArrayInputStream(byteArray);
            compressedResult = objectStorageService.upload(compressedIs, file.getName() + "-compressed", byteArray.length, file.getContentType());

            Image newImage = Image.builder()
                    .uri(String.format("%s/%s/%s", minioProperties.publicEndpoint(), minioProperties.bucket(), result.getObjectName()))
                    .thumbnailUri(String.format("%s/%s/%s", minioProperties.publicEndpoint(), minioProperties.bucket(), compressedResult.getObjectName()))
                    .build();

            imageRepository.save(newImage);

            return FileUploadResponseDto.builder()
                    .fileId(newImage.getId().toString())
                    .fileUri(newImage.getUri())
                    .fileThumbnailUri(newImage.getThumbnailUri())
                    .build();
        } catch (Exception e) {
            // TODO: Remove uploaded file
            throw new RuntimeException("System failed to upload the file", e);
        }
    }
}
