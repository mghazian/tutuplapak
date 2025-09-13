package com.coffeeteam.tutuplapak.profile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeeteam.tutuplapak.auth.security.CustomUserDetails;
import com.coffeeteam.tutuplapak.profile.dto.EmailLinkRequestBody;
import com.coffeeteam.tutuplapak.profile.dto.PhoneLinkRequestBody;
import com.coffeeteam.tutuplapak.profile.dto.ProfileRequestBody;
import com.coffeeteam.tutuplapak.profile.dto.ProfileResponseBody;
import com.coffeeteam.tutuplapak.profile.service.ProfileService;

@RestController
@RequestMapping("/v1")
public class ProfileController {
    
    @Autowired
    private ProfileService profileService;

    @GetMapping("/user")
    public ResponseEntity<ProfileResponseBody> getProfile(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ProfileResponseBody body = profileService.getProfile(userDetails.user().getId());

        return ResponseEntity.ok().body(body);
    }

    public ResponseEntity<ProfileResponseBody> updateProfile(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody ProfileRequestBody requestBody
    ) {
        ProfileResponseBody body = profileService.updateProfile(userDetails.user().getId(), requestBody);

        return ResponseEntity.ok().body(body);
    }

    @PostMapping("/user/link/phone")
    public ResponseEntity<ProfileResponseBody> linkPhone(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody PhoneLinkRequestBody requestBody
    ) {
        ProfileResponseBody body = profileService.linkPhone(userDetails.user().getId(), requestBody.getPhone());
        
        return ResponseEntity.ok().body(body);
    }

    @PostMapping("/user/link/email")
    public ResponseEntity<ProfileResponseBody> linkEmail(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody EmailLinkRequestBody requestBody
    ) {
        ProfileResponseBody body = profileService.linkEmail(userDetails.user().getId(), requestBody.getEmail());
        
        return ResponseEntity.ok().body(body);
    }
}
