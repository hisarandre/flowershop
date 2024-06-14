package com.piba.flowershop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Gender cannot be blank")
    private String gender;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

}
