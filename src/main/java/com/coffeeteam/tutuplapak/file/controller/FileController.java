package com.coffeeteam.tutuplapak.file.controller;

import com.coffeeteam.tutuplapak.auth.UserClaim;
import com.coffeeteam.tutuplapak.file.dto.FileUploadResponseDto;
import com.coffeeteam.tutuplapak.file.service.FileUploadService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/v1/file")
public class FileController {

    private final FileUploadService fileUploadService;

    public FileController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping
    ResponseEntity<FileUploadResponseDto> upload (
            HttpServletRequest httpServletRequest,
            MultipartFile file,
            @AuthenticationPrincipal UserClaim userDetail
            ) {
        FileUploadResponseDto response = fileUploadService.upload(file, userDetail);
        return ResponseEntity.of(Optional.of(response));
    }
}
