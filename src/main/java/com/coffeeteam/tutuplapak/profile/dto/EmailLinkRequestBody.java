package com.coffeeteam.tutuplapak.profile.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailLinkRequestBody {
    @NotBlank
    @Email
    private String email;
}
