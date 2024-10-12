package com.emrheathgroup.backend.DTOs.Login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDataDto {

	@NotBlank(message = "Username is required")
	private String userName;

	@NotBlank(message = "Email is required")
	private String email;

	@NotNull(message = "Phone number is required")
	private String phoneNo;

	@NotBlank(message = "Password is required")
	private String password;

	@NotBlank(message = "Confirm password is required")
	private String confirmPassword;

	@NotNull(message = "Role ID is required")
	private Integer roleId;

	@NotNull(message = "Branch ID is required")
	private Integer branchId;

	

}
