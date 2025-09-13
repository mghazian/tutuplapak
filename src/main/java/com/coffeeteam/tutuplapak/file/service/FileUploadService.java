package com.coffeeteam.tutuplapak.file.service;

import com.coffeeteam.tutuplapak.auth.security.CustomUserDetails;
import com.coffeeteam.tutuplapak.file.dto.FileUploadRequestDto;
import com.coffeeteam.tutuplapak.file.dto.FileUploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    FileUploadResponseDto upload(MultipartFile file, CustomUserDetails userDetails);
}
