package com.coffeeteam.tutuplapak.minio;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public record MinioProperties (
        String endpoint,
        String publicEndpoint,
        String accessKey,
        String secretKey,
        boolean secure,
        long presignExpirySeconds,
        String bucket
) {}
