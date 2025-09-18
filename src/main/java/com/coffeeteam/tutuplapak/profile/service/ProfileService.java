package com.coffeeteam.tutuplapak.profile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.coffeeteam.tutuplapak.core.entity.User;
import com.coffeeteam.tutuplapak.file.exception.FileNotFoundException;
import com.coffeeteam.tutuplapak.file.model.Image;
import com.coffeeteam.tutuplapak.file.repository.ImageRepository;
import com.coffeeteam.tutuplapak.profile.dto.ProfileRequestBody;
import com.coffeeteam.tutuplapak.profile.dto.ProfileResponseBody;
import com.coffeeteam.tutuplapak.profile.repository.ProfileRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProfileService {
    
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ImageRepository imageRepository;

    public ProfileResponseBody getProfile(Long userId) {
        User savedUser = profileRepository.findByIdWithImage(userId).orElseThrow(() -> new EntityNotFoundException("user missing"));
        
        return new ProfileResponseBody(
            savedUser.getEmail() != null ? savedUser.getEmail() : "", 
            savedUser.getPhone() != null ? savedUser.getPhone() : "", 
            savedUser.getImage() != null ? savedUser.getImage().getId().toString() : "", 
            savedUser.getImage() != null ? savedUser.getImage().getUri().toString() : "", 
            savedUser.getImage() != null ? savedUser.getImage().getThumbnailUri().toString() : "", 
            savedUser.getBankAccountName(), 
            savedUser.getBankAccountHolder(), 
            savedUser.getBankAccountNumber());
    }

    public ProfileResponseBody updateProfile(Long userId, ProfileRequestBody requestBody) throws FileNotFoundException {
        long fileId;
        try {
            fileId = Long.parseLong(requestBody.getFileId());
        } catch (NumberFormatException e) {
            throw new FileNotFoundException();
        }

        User savedUser = profileRepository.findByIdWithImage(userId).orElseThrow(() -> new EntityNotFoundException("user missing"));

        Image image = imageRepository.findById(fileId).orElseThrow(() -> new FileNotFoundException());

        savedUser.setImage(image);
        savedUser.setBankAccountName(requestBody.getBankAccountName());
        savedUser.setBankAccountHolder(requestBody.getBankAccountHolder());
        savedUser.setBankAccountNumber(requestBody.getBankAccountNumber());
        User result = profileRepository.save(savedUser);

        return new ProfileResponseBody(
            result.getEmail() != null ? result.getEmail() : "", 
            result.getPhone() != null ? result.getPhone() : "", 
            image != null ? image.getId().toString() : "", 
            image != null ? image.getUri().toString() : "", 
            image != null ? image.getThumbnailUri().toString() : "", 
            result.getBankAccountName(), 
            result.getBankAccountHolder(), 
            result.getBankAccountNumber());
    }

    public ProfileResponseBody linkPhone(Long userId, String phone) throws DataIntegrityViolationException, EntityNotFoundException {
        User savedUser = profileRepository.findByIdWithImage(userId).orElseThrow(() -> new EntityNotFoundException());

        savedUser.setPhone(phone);
        User result = profileRepository.save(savedUser);
        
        return new ProfileResponseBody(
            result.getEmail() != null ? result.getEmail() : "", 
            result.getPhone() != null ? result.getPhone() : "", 
            result.getImage() != null ? result.getImage().getId().toString() : "", 
            result.getImage() != null ? result.getImage().getUri().toString() : "", 
            result.getImage() != null ? result.getImage().getThumbnailUri().toString() : "", 
            result.getBankAccountName(),
            result.getBankAccountHolder(),
            result.getBankAccountNumber()
        );
    }

    public ProfileResponseBody linkEmail(Long userId, String email) throws DataIntegrityViolationException, EntityNotFoundException {
        User savedUser = profileRepository.findByIdWithImage(userId).orElseThrow(() -> new EntityNotFoundException());

        savedUser.setEmail(email);
        User result = profileRepository.save(savedUser);
        
        return new ProfileResponseBody(
            result.getEmail() == null ? "" : result.getEmail(),
            result.getPhone() == null ? "" : result.getPhone(),
            result.getImage() != null ? result.getImage().getId().toString() : "", 
            result.getImage() != null ? result.getImage().getUri().toString() : "", 
            result.getImage() != null ? result.getImage().getThumbnailUri().toString() : "", 
            result.getBankAccountName(),
            result.getBankAccountHolder(),
            result.getBankAccountNumber()
        );
    }

}
