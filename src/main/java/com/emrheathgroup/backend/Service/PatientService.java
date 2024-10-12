package com.emrheathgroup.backend.Service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.PatientDTOs.DocumentRequest;
import com.emrheathgroup.backend.DTOs.PatientDTOs.PatientRegistrationRequestDto;
import com.emrheathgroup.backend.Entity.Patient;

import jakarta.validation.Valid;

@Component
public interface PatientService {

	ResponseDTO getPatientByNationalId(String nationalId);

	ResponseDTO getPatientByMrdNo(String mrdNo);

	ResponseDTO getPatientByEmailId(String email);

	ResponseDTO getPatientByPhoneNo(String phoneNo);

	ResponseDTO getPatientByName(String name);

//	ResponseDTO mobPatientRegistration(Integer step, MultipartFile profile, List<MultipartFile> document,
//			PatientRegistrationRequestDto requestDto);

	Page<Patient> getPatientsWithFilters(String mrdNo, String patientName, String email, String phoneNo,
			String nationalId, int page, int size);

	ResponseDTO mobPatientRegistration(PatientRegistrationRequestDto patientRequest, MultipartFile profileImage);

	ResponseDTO uploadPatientDocument(@Valid DocumentRequest request);

	ResponseDTO searchPatients(String searchKey, int page, int size);

}
