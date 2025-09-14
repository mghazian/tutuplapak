package com.coffeeteam.tutuplapak.purchase.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentProofRequest {

    @NotNull(message = "fileIds cannot be null")
    @NotEmpty(message = "fileIds must contain at least one fileId")
    private List<@jakarta.validation.constraints.NotBlank(message = "fileId cannot be blank") String> fileIds;
}