package com.coffeeteam.tutuplapak.product.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Category {
    FOOD("Food"),
    BEVERAGE("Beverage"),
    CLOTHES("Clothes"),
    FURNITURE("Furniture"),
    TOOLS("Tools");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    @Override
    @JsonValue
    public String toString() {
        return displayName;
    }
}
