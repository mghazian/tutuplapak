package com.coffeeteam.tutuplapak.purchase.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

@Getter
@Setter
public class PurchaseRequest {

    @Valid
    @NotNull(message = "purchasedItems cannot be null")
    @Size(min = 1, message = "purchasedItems must have at least 1 item")
    private List<@Valid PurchasedItemRequest> purchasedItems;

    @NotBlank(message = "senderName is required")
    @Size(min = 4, max = 55, message = "senderName must be between 4 and 55 characters")
    @JsonDeserialize(using = StringOnlyDeserializer.class)
    private String senderName;

    @NotBlank(message = "senderContactType is required")
    @Pattern(regexp = "^(email|phone)$", message = "senderContactType must be either 'email' or 'phone'")
    private String senderContactType;

    @NotBlank(message = "senderContactDetail is required")
    @JsonDeserialize(using = StringOnlyDeserializer.class)
    private String senderContactDetail;
}

class StringOnlyDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        if (node.isBoolean()) {
            throw new IllegalArgumentException("Field must be a string value, not boolean");
        }

        if (!node.isTextual()) {
            throw new IllegalArgumentException("Field must be a string value");
        }

        return node.asText();
    }
}