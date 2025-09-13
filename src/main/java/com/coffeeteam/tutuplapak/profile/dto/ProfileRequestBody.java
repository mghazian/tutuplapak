package com.coffeeteam.tutuplapak.profile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileRequestBody {
    private String fileId;
    @NotBlank
    @Size(min = 4, max = 32)
    private String bankAccountName;
    @NotBlank
    @Size(min = 4, max = 32)
    private String bankAccountHolder;
    @NotBlank
    @Size(min = 4, max = 32)
    private String bankAccountNumber;
}
