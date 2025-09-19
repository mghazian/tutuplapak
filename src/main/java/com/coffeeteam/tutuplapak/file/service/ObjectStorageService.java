package com.coffeeteam.tutuplapak.file.service;

import com.coffeeteam.tutuplapak.file.dto.UploadResultDto;

import java.io.InputStream;

public interface ObjectStorageService {
    UploadResultDto upload(InputStream inputStream, String filename, long size, String contentType);
}
