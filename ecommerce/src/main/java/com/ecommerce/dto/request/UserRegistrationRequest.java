package com.ecommerce.dto.request;

import com.ecommerce.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {

    @Size(max = 255)
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Size(max = 155)
    private String login;
    @NotNull
    @NotBlank
    @Size(max = 155)
    private String password;
    private Role role;
}
