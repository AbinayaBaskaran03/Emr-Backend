package com.emrheathgroup.backend.Service;

import org.springframework.stereotype.Component;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.Login.SignInDataDto;
import com.emrheathgroup.backend.DTOs.Login.SignUpDataDto;

import jakarta.validation.Valid;

@Component
public interface AuthService {

	ResponseDTO signUp(SignUpDataDto data);

	ResponseDTO forgotPassword(String email);

	ResponseDTO resetPassword(String token, String newPassword, String confirmPassword);

	ResponseDTO signInWithEmail(@Valid SignInDataDto data);

	ResponseDTO signInWithUsername(@Valid SignInDataDto data);

}
