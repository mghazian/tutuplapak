package com.coffeeteam.tutuplapak.file.service;

import com.coffeeteam.tutuplapak.file.dto.UploadResultDto;
import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageService {
    UploadResultDto upload (MultipartFile file);
}
