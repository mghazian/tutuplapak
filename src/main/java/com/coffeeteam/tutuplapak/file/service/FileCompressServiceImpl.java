package com.coffeeteam.tutuplapak.file.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class FileCompressServiceImpl implements FileCompressService {
    @Override
    public byte[] compress(byte[] file) {
        ByteArrayInputStream is = new ByteArrayInputStream(file);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            Thumbnails.of(is)
                    .size(100, 100)
                    .outputQuality(0.25f)
                    .toOutputStream(os);

            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
