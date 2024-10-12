package com.emrheathgroup.backend.Controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.Login.SignInDataDto;
import com.emrheathgroup.backend.DTOs.Login.SignUpDataDto;
import com.emrheathgroup.backend.Exception.InvalidCredentialsException;
import com.emrheathgroup.backend.Exception.UserNotFoundException;
import com.emrheathgroup.backend.Helper.StaticConstants;
import com.emrheathgroup.backend.Service.AuthService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<ResponseDTO> signUp(@RequestBody @Valid SignUpDataDto data) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO = authService.signUp(data);
			if (responseDTO.getStatus()) {
				return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			responseDTO = new ResponseDTO();
			responseDTO.setMessage("Exception Occurred: " + e.getMessage());
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatus(false);
			return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/signin")
	public ResponseEntity<ResponseDTO> signin(@RequestBody @Valid SignInDataDto data) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			if (data.getEmail() != null && !data.getEmail().isEmpty()) {
				responseDTO = authService.signInWithEmail(data);
			} else if (data.getUserName() != null && !data.getUserName().isEmpty()) {
				responseDTO = authService.signInWithUsername(data);
			} else {
				responseDTO.setMessage("Email or Username is required.");
				responseDTO.setStatus(false);
				return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (UserNotFoundException exception) {
			responseDTO.setMessage("Exception Occurred: " + exception.getMessage());
			responseDTO.setStatus(false);
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
		} catch (InvalidCredentialsException exception) {
			responseDTO.setMessage("Exception Occurred: " + exception.getMessage());
			responseDTO.setStatus(false);
			return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			responseDTO.setMessage("Exception Occurred: " + e.getMessage());
			responseDTO.setStatus(false);
			return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/forgotPassword")
	public ResponseEntity<ResponseDTO> forgotPassword(@RequestParam String email) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			responseDTO = authService.forgotPassword(email);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Exception Occured");
			responseDTO.setStatus(false);
		} finally {
			return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
		}
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<ResponseDTO> resetPassword(@RequestParam String token, @RequestParam String newPassword,
			@RequestParam String confirmPassword) {

		ResponseDTO responseDTO = new ResponseDTO();

		try {
			responseDTO = authService.resetPassword(token, newPassword, confirmPassword);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Exception Occurred");
		} finally {
			return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
		}
	}

}
