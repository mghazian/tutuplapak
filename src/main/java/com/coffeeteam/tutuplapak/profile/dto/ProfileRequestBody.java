package com.coffeeteam.tutuplapak.profile.dto;

import com.coffeeteam.tutuplapak.core.deserializer.StrictStringDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileRequestBody {
    @NotBlank
    @JsonDeserialize(using = StrictStringDeserializer.class)
    private String fileId;
    @NotBlank
    @Size(min = 4, max = 32)
    @JsonDeserialize(using = StrictStringDeserializer.class)
    private String bankAccountName;
    @NotBlank
    @Size(min = 4, max = 32)
    @JsonDeserialize(using = StrictStringDeserializer.class)
    private String bankAccountHolder;
    @NotBlank
    @Size(min = 4, max = 32)
    @JsonDeserialize(using = StrictStringDeserializer.class)
    private String bankAccountNumber;
}
