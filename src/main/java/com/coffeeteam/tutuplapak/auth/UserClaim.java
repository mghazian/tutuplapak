package com.coffeeteam.tutuplapak.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Dear mas @anas mas @roni, nanti di controller
 * punya kalian ini yang dipake buat @AuthenticationPrincipal ya
 * gua juga belom coba sih wkwkwk nanti kalo ngawur kabarin gua ASAP
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserClaim {
    private Long id;

    @Deprecated
    private String phone;

    @Deprecated
    private String email;
}
