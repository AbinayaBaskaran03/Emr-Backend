package com.emrheathgroup.backend.DTOs.Login;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInDataDto {

	private String email;
	
    @NotBlank(message = "Password is required")
	private String password;

	private String userName;
	
}
