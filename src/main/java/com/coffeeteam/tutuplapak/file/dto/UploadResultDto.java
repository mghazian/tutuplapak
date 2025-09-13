package com.coffeeteam.tutuplapak.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadResultDto {
    String bucket;
    String objectName;
    String contentType;
    long size;
    String etag;
    String presignedUrl;
}