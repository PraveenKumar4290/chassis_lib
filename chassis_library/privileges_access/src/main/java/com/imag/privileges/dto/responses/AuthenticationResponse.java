package com.imag.privileges.dto.responses;


import com.imag.privileges.model.Privilege;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private Set<Privilege> privileges;
    private String token;
    private String refreshToken;
}
