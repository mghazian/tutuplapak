package com.coffeeteam.tutuplapak.profile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PhoneLinkRequestBody {
    @NotBlank
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Invalid international phone number")
    private String phone;
}
