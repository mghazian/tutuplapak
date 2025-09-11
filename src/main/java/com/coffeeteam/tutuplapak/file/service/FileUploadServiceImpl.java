package com.coffeeteam.tutuplapak.file.service;

import com.coffeeteam.tutuplapak.file.dto.FileUploadRequestDto;
import com.coffeeteam.tutuplapak.file.dto.FileUploadResponseDto;
import com.coffeeteam.tutuplapak.file.dto.UploadResultDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final ObjectStorageService objectStorageService;

    public FileUploadServiceImpl(ObjectStorageService objectStorageService) {
        this.objectStorageService = objectStorageService;
    }

    @Override
    public FileUploadResponseDto upload(MultipartFile file) {
        UploadResultDto result = objectStorageService.upload(file);

        return FileUploadResponseDto.builder()
                .fileId("1")
                .fileUri(result.getPresignedUrl())
                .build();
    }
}
