package com.emrheathgroup.backend.DTOs.PatientDTOs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.emrheathgroup.backend.Enumeration.Gender;
import com.emrheathgroup.backend.Enumeration.MaritalStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRegistrationRequestDto {

	private Integer step;

    private MultipartFile profile;  
    
	private String nationalId;
	private String mrdNo;
	private String name;
	
	private Date dob;
	
	private Integer age;
	private String nationality;
	private Gender gender;
	private String emailId;
	private MaritalStatus maritalStatus;
	private String visaType;
	private String otherId;
	private String occupation;
	private String address;
	private String phoneNo;
	private String insuranceName;
	private String problem;
	private String symptoms;

	private List<DependentRequest> dependentList = new ArrayList<>();
    private List<DocumentRequest> documentList = new ArrayList<>();

	
}
