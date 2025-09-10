package com.coffeeteam.tutuplapak.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserClaim {
    private Long id;
    private String phone;
    private String email;
}
