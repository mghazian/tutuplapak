package com.coffeeteam.tutuplapak.profile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.coffeeteam.tutuplapak.core.entity.User;
import com.coffeeteam.tutuplapak.profile.dto.ProfileRequestBody;
import com.coffeeteam.tutuplapak.profile.dto.ProfileResponseBody;
import com.coffeeteam.tutuplapak.profile.repository.ProfileRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProfileService {
    
    @Autowired
    private ProfileRepository profileRepository;

    public ProfileResponseBody getProfile(Long userId) {
        User savedUser = profileRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user missing"));
        
        return new ProfileResponseBody(
            savedUser.getEmail() != null ? savedUser.getEmail().toString() : "", 
            savedUser.getPhone() != null ? savedUser.getPhone().toString() : "", 
            savedUser.getImageId() != null ? savedUser.getImageId().toString() : "", 
            "", 
            "", 
            savedUser.getBankAccountName(), 
            savedUser.getBankAccountHolder(), 
            savedUser.getBankAccountNumber());
    }

    public ProfileResponseBody updateProfile(Long userId, ProfileRequestBody requestBody) {
        long fileId;
        try {
            fileId = Long.parseLong(requestBody.getFileId());
        } catch (NumberFormatException e) {
            fileId = -1;
        }

        User savedUser = profileRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user missing"));

        savedUser.setImageId(fileId);
        savedUser.setBankAccountName(requestBody.getBankAccountName());
        savedUser.setBankAccountHolder(requestBody.getBankAccountHolder());
        savedUser.setBankAccountNumber(requestBody.getBankAccountNumber());
        User result = profileRepository.save(savedUser);
        
        return new ProfileResponseBody(
            savedUser.getEmail(), 
            savedUser.getPhone(), 
            requestBody.getFileId(), 
            "", 
            "", 
            result.getBankAccountName(), 
            result.getBankAccountHolder(), 
            result.getBankAccountNumber());
    }

    public ProfileResponseBody linkPhone(Long userId, String phone) throws DataIntegrityViolationException, EntityNotFoundException {
        User savedUser = profileRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException());

        savedUser.setPhone(phone);
        User result = profileRepository.save(savedUser);
        
        return new ProfileResponseBody(
            result.getEmail() == null ? "" : result.getEmail(),
            result.getPhone() == null ? "" : result.getPhone(),
            "",
            "",
            "",
            savedUser.getBankAccountName(),
            savedUser.getBankAccountHolder(),
            savedUser.getBankAccountNumber()
        );
    }

    public ProfileResponseBody linkEmail(Long userId, String email) throws DataIntegrityViolationException, EntityNotFoundException {
        User savedUser = profileRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException());

        savedUser.setEmail(email);
        User result = profileRepository.save(savedUser);
        
        return new ProfileResponseBody(
            result.getEmail() == null ? "" : result.getEmail(),
            result.getPhone() == null ? "" : result.getPhone(),
            "",
            "",
            "",
            savedUser.getBankAccountName(),
            savedUser.getBankAccountHolder(),
            savedUser.getBankAccountNumber()
        );
    }

}
