package com.coffeeteam.tutuplapak.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseBody {
    private String email;
    private String phone;
    private String fileId;
    private String fileUri;
    private String fileThumbnailUri;
    private String bankAccountName;
    private String bankAccountHolder;
    private String bankAccountNumber;
}
