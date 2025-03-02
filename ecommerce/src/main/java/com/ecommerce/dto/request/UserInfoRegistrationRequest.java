package com.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRegistrationRequest {

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String surname;
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String phone;
}
