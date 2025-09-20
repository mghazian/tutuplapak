package com.coffeeteam.tutuplapak.file.exception;

public class FileNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "file not found";
    }
    
}
