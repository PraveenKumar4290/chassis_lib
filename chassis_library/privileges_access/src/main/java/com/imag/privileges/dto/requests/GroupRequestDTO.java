package com.imag.privileges.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequestDTO {

    @NotBlank(message = "Group name is mandatory")
    private String name;
    private String description;
}
