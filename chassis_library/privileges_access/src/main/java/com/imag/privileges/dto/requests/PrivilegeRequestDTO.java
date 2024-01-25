package com.imag.privileges.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrivilegeRequestDTO {
    @NotBlank(message = "Privilege name is mandatory")
    private String name;
    private String description;
    private Long parentId;
    private String code;
}
