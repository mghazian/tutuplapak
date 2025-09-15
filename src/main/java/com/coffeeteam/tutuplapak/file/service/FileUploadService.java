package com.coffeeteam.tutuplapak.file.service;

import com.coffeeteam.tutuplapak.auth.UserClaim;
import com.coffeeteam.tutuplapak.file.dto.FileUploadRequestDto;
import com.coffeeteam.tutuplapak.file.dto.FileUploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    FileUploadResponseDto upload(MultipartFile file, UserClaim userDetails);
}
