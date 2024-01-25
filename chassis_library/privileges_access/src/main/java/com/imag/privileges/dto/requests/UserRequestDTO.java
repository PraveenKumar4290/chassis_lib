package com.imag.privileges.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRequestDTO {


    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = "[A-Z]{6,20}",message = "Invalid username")
    private String username;
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@$!%*?&])[A-Za-z\\\\d@$!%*?&]{8,}$",message = "Password should be strong")
    private String password;
    @Email(message = "Email must be valid")
    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message = "Invalid email address.")
    @NotBlank(message = "email is mandatory")
    private String email;
    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^(?:\\+?1[-.\\s]?)?([0-9]{3})[\\s.-]?([0-9]{3})[\\s.-]?([0-9]{4})$",message = "Invalid phone number")
    private String phoneNumber;
}
