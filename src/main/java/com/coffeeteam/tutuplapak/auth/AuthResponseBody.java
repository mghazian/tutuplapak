package com.coffeeteam.tutuplapak.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseBody {
    private String email;
    private String phone;
    private String token;
}
