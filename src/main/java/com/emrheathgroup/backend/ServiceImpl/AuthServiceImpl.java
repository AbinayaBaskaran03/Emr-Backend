package com.emrheathgroup.backend.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.Login.SignInDataDto;
import com.emrheathgroup.backend.DTOs.Login.SignUpDataDto;
import com.emrheathgroup.backend.Entity.BranchMaster;
import com.emrheathgroup.backend.Entity.RoleMaster;
import com.emrheathgroup.backend.Entity.UserTable;
import com.emrheathgroup.backend.Repository.BranchRepo;
import com.emrheathgroup.backend.Repository.RoleRepo;
import com.emrheathgroup.backend.Repository.UserRepo;
import com.emrheathgroup.backend.Security.JWTUtil;
import com.emrheathgroup.backend.Service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepo userRepo;

	@Autowired(required = true)
	private JavaMailSender emailSender;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private BranchRepo branchRepo;

	@Autowired
	private JWTUtil jwtUtil;

	private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

	@Override
	public ResponseDTO signUp(SignUpDataDto data) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (!data.getPassword().equals(data.getConfirmPassword())) {
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Password and Confirm Password do not match");
			return responseDTO;
		}

		if (userRepo.findByUserName(data.getUserName()) != null) {
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("User Name Already Exists");
			return responseDTO;
		}

		if (userRepo.findByPhoneNo(data.getPhoneNo()) != null) {
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Phone Number Already Exists");
			return responseDTO;
		}

		if (userRepo.findByEmail(data.getEmail()) != null) {
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Email Already Exists");
			return responseDTO;
		}

		RoleMaster roleMaster = roleRepo.findById(data.getRoleId()).orElse(null);
		if (roleMaster == null) {
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Role Not Found");
			return responseDTO;
		}

		BranchMaster branchMaster = branchRepo.findById(data.getBranchId()).orElse(null);
		if (branchMaster == null) {
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Branch Not Found");
			return responseDTO;
		}

		UserTable user = new UserTable();
		user.setEmail(data.getEmail().trim());
		user.setPhoneNo(data.getPhoneNo().trim());
		user.setUserName(data.getUserName().trim());
		String encodedPassword = passwordEncoder.encode(data.getPassword());
		user.setPassword(encodedPassword);
		user.setRoleMaster(roleMaster);
		user.setBranchMaster(branchMaster);

		userRepo.save(user);

		responseDTO.setData(user);
		responseDTO.setStatus(true);
		responseDTO.setMessage("User Created Successfully");

		return responseDTO;
	}

	@Override
	public ResponseDTO signInWithEmail(SignInDataDto data) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			UserTable user = userRepo.findByEmail(data.getEmail());
			if (user == null) {
				responseDTO.setStatus(false);
				responseDTO.setMessage("Email not found");
				return responseDTO;
			}
			if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
				responseDTO.setStatus(false);
				responseDTO.setMessage("Incorrect password");
				return responseDTO;
			}
			responseDTO.setStatus(true);
			responseDTO.setMessage("User logged in successfully");
			responseDTO.setData(user);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus(false);
			responseDTO.setMessage("An exception occurred");
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO signInWithUsername(SignInDataDto data) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			UserTable user = userRepo.findByUserName(data.getUserName());
			if (user == null) {
				responseDTO.setStatus(false);
				responseDTO.setMessage("Username not found");
				return responseDTO;
			}
			if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
				responseDTO.setStatus(false);
				responseDTO.setMessage("Incorrect password");
				return responseDTO;
			}
			responseDTO.setStatus(true);
			responseDTO.setMessage("User logged in successfully");
			responseDTO.setData(user);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus(false);
			responseDTO.setMessage("An exception occurred");
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO forgotPassword(String email) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			UserTable user = userRepo.findByEmail(email);
			if (user == null) {
				responseDTO.setStatus(false);
				responseDTO.setData(new ArrayList<>());
				responseDTO.setStatusCode(404);
				responseDTO.setMessage("User Not Found");
				return responseDTO;
			}

			String resetToken = UUID.randomUUID().toString();
			user.setResetToken(resetToken);
			userRepo.save(user);

			String resetLink = "http://localhost:3000/reset-password?token=" + resetToken;

			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("abinayabaskaran1012@gmail.com");
			message.setTo(email);
			message.setSubject("Reset Password");
			message.setText("Click on the following link to reset your password: " + resetLink);
			emailSender.send(message);

			responseDTO.setStatus(true);
			responseDTO.setData(user);
			responseDTO.setStatusCode(200);
			responseDTO.setMessage("Email Sent Successfully");
			log.info("Email Sent Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Exception Occurred");
		} finally {
			return responseDTO;
		}
	}

	@Override
	public ResponseDTO resetPassword(String token, String newPassword, String confirmPassword) {
		ResponseDTO responseDTO = new ResponseDTO();

		UserTable user = userRepo.findByResetToken(token);
		if (user == null) {
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(404);
			responseDTO.setMessage("Invalid or expired token");
			return responseDTO;
		}

		if (!newPassword.equals(confirmPassword)) {
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(400);
			responseDTO.setMessage("Passwords do not match");
			return responseDTO;
		}

		if (passwordEncoder.matches(newPassword, user.getPassword())) {
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(400);
			responseDTO.setMessage("New password must not be the same as the old password");
			return responseDTO;
		}

		List<UserTable> allUsers = userRepo.findAll();
		for (UserTable otherUser : allUsers) {
			if (passwordEncoder.matches(newPassword, otherUser.getPassword())) {
				responseDTO.setStatus(false);
				responseDTO.setData(new ArrayList<>());
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("New password must not match any other user's password");
				return responseDTO;
			}
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		user.setResetToken(null);
		userRepo.save(user);

		responseDTO.setStatus(true);
		responseDTO.setData(user);
		responseDTO.setStatusCode(200);
		responseDTO.setMessage("Password Reset Successfully");
		log.info("Password Reset Successfully");

		return responseDTO;
	}

}
