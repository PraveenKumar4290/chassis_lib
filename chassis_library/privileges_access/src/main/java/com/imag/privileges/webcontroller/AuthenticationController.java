package com.imag.privileges.webcontroller;

import com.imag.privileges.dto.requests.LoginRequest;
import com.imag.privileges.dto.requests.RefreshTokenRequest;
import com.imag.privileges.dto.responses.AuthenticationResponse;
import com.imag.privileges.exceptions.UserDefinedException;
import com.imag.privileges.services.JwtLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private JwtLoginService jwtLoginService;

    @PostMapping("/user/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest) {
        try {
            AuthenticationResponse authResponse = jwtLoginService.tokenGenerate(loginRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credential.");
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest tokenRequest) {
        try {
            AuthenticationResponse authResponse = jwtLoginService.refreshTokenGenerate(tokenRequest);
            return ResponseEntity.status(HttpStatus.OK).body(authResponse);
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
