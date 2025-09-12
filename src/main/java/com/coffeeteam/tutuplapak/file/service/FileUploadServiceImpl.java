package com.coffeeteam.tutuplapak.file.service;

import com.coffeeteam.tutuplapak.file.dto.FileUploadRequestDto;
import com.coffeeteam.tutuplapak.file.dto.FileUploadResponseDto;
import com.coffeeteam.tutuplapak.file.dto.UploadResultDto;
import com.coffeeteam.tutuplapak.file.model.Image;
import com.coffeeteam.tutuplapak.file.repository.ImageRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

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

    private byte[] compress(MultipartFile file) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Thumbnails.of(file.getInputStream())
                .size(100, 100)
                .outputQuality(0.25f)
                .toOutputStream(os);

        return os.toByteArray();
    }

    @Override
    public FileUploadResponseDto upload(MultipartFile file) {
        UploadResultDto result = null;
        UploadResultDto compressedResult = null;
        try (InputStream is = file.getInputStream()) {
            result = objectStorageService.upload(is, file.getName(), file.getSize(), file.getContentType());

            byte[] byteArray = compress(file);
            InputStream compressedIs = new ByteArrayInputStream(byteArray);
            compressedResult = objectStorageService.upload(compressedIs, file.getName() + "-compressed", byteArray.length, file.getContentType());

            Image newImage = Image.builder()
                    .ownerId(1L)
                    .uri(result.getObjectName())
                    .thumbnailUri(compressedResult.getObjectName())
                    .build();

            imageRepository.save(newImage);

            return FileUploadResponseDto.builder()
                    .fileId("1")
                    .fileUri(newImage.getUri())
                    .fileThumbnailUri(newImage.getThumbnailUri())
                    .build();
        } catch (Exception e) {
            // TODO: Remove uploaded file
            throw new RuntimeException("System failed to upload the file", e);
        }
    }
}
