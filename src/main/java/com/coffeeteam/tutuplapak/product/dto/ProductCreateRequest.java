package com.coffeeteam.tutuplapak.product.dto;

import com.coffeeteam.tutuplapak.core.deserializer.StrictStringDeserializer;
import com.coffeeteam.tutuplapak.product.enums.Category;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record ProductCreateRequest(
        @NotBlank
        @Size(min = 4, max = 32)
        @JsonDeserialize(using = StrictStringDeserializer.class)
        String name,

        @NotNull
        Category category,

        @NotNull
        @Min(1)
        Integer qty,

        @NotNull
        @Min(100)
        Integer price,

        @NotBlank
        @Size(max = 32)
        @JsonDeserialize(using = StrictStringDeserializer.class)
        String sku,

        @NotNull
        @JsonDeserialize(using = StrictStringDeserializer.class)
        String fileId
) {
        public String getFileId() {
                return fileId;
        }
}
