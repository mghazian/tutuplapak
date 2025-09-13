package com.coffeeteam.tutuplapak.core.exceptions;

public class ConflictException extends Exception{

    @Override
    public String getMessage() {
        return "credential conflict";
    }
    
}
