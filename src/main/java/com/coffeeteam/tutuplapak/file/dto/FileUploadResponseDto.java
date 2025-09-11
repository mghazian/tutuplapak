package com.coffeeteam.tutuplapak.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadResponseDto {
    String fileId;
    String fileUri;
    String fileThumbnailUri;
}
