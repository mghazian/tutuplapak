package com.coffeeteam.tutuplapak.core.exceptions;

public class InvalidCredentialsException extends Exception{

    @Override
    public String getMessage() {
        return "invalid credentials";
    }
    
}
