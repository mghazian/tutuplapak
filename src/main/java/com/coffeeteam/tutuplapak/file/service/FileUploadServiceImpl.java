package com.coffeeteam.tutuplapak.file.service;

import com.coffeeteam.tutuplapak.file.dto.FileUploadRequestDto;
import com.coffeeteam.tutuplapak.file.dto.FileUploadResponseDto;
import com.coffeeteam.tutuplapak.file.dto.UploadResultDto;
import com.coffeeteam.tutuplapak.file.model.Image;
import com.coffeeteam.tutuplapak.file.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final ObjectStorageService objectStorageService;
    private final ImageRepository imageRepository;

    public FileUploadServiceImpl(
            ObjectStorageService objectStorageService,
            ImageRepository imageRepository
    ) {
        this.objectStorageService = objectStorageService;
        this.imageRepository = imageRepository;
    }

    @Override
    public FileUploadResponseDto upload(MultipartFile file) {
        try {
            UploadResultDto result = objectStorageService.upload(file);

            Image newImage = Image.builder()
                    .ownerId(1L)
                    .uri(result.getObjectName())
                    .thumbnailUri(result.getObjectName())
                    .build();

            imageRepository.save(newImage);

            return FileUploadResponseDto.builder()
                    .fileId("1")
                    .fileUri(newImage.getUri())
                    .fileThumbnailUri(newImage.getThumbnailUri())
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }
}
