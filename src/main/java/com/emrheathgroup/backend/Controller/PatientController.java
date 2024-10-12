package com.emrheathgroup.backend.Controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.PatientDTOs.DocumentRequest;
import com.emrheathgroup.backend.DTOs.PatientDTOs.PatientRegistrationRequestDto;
import com.emrheathgroup.backend.Entity.Patient;
import com.emrheathgroup.backend.Helper.StaticConstants;
import com.emrheathgroup.backend.Service.DoctorAppointmentService;
import com.emrheathgroup.backend.Service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/patient")
public class PatientController {

	@Autowired
	private PatientService patientService;

	@Autowired
	private DoctorAppointmentService doctorAppointmentService;

	@ApiOperation(value = "Get Patient records by using nationalId")
	@GetMapping("/getPatientByNationalId/{nationalId}")
	public ResponseEntity<ResponseDTO> getPatientByNationalId(@PathVariable String nationalId) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			responseDTO = patientService.getPatientByNationalId(nationalId);
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

	@ApiOperation(value = "Get Patient records by using mrdNo")
	@GetMapping("/getPatientByMrdNo")
	public ResponseEntity<ResponseDTO> getPatientByMrdNo(@RequestParam String mrdNo) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			responseDTO = patientService.getPatientByMrdNo(mrdNo);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Exception Occurred");
			responseDTO.setStatus(false);
		} finally {
			return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
		}
	}

	@ApiOperation(value = "Get Patient records by using Email")
	@GetMapping("/getPatientByEmail")
	public ResponseEntity<ResponseDTO> getPatientByEmail(@RequestParam String email) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			responseDTO = patientService.getPatientByEmailId(email);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Exception Occurred");
			responseDTO.setStatus(false);
		} finally {
			return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
		}
	}

	@ApiOperation(value = "Get Patient records by using phoneNo")
	@GetMapping("/getPatientByMobile")
	public ResponseEntity<ResponseDTO> getPatientByPhoneNo(@RequestParam String phoneNo) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			responseDTO = patientService.getPatientByPhoneNo(phoneNo);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Exception Occurred");
			responseDTO.setStatus(false);
		} finally {
			return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
		}
	}

	@ApiOperation(value = "Get Patient records by using name")
	@GetMapping("/getPatientByName")
	public ResponseEntity<ResponseDTO> getPatientByName(@RequestParam String name) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			responseDTO = patientService.getPatientByName(name);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Exception Occurred");
			responseDTO.setStatus(false);
		} finally {
			return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
		}
	}

	@ApiOperation(value = "Fetch Patients by Doctor ID with Filters")
	@GetMapping("/getPatientsByDocIdWithFilters")
	public ResponseEntity<ResponseDTO> getPatientsByDocIdWithFilters(@RequestParam Integer doctorId,
			@RequestParam String filters) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO = doctorAppointmentService.getPatientsByDocIdWithFilters(doctorId, filters);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Exception Occurred");
			responseDTO.setStatus(false);
		} finally {
			return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
		}
	}

	@PostMapping("/mobPatientRegistration")
	public ResponseEntity<ResponseDTO> mobPatientRegistration(@RequestParam("profileImage") MultipartFile profileImage,
			@RequestParam("patientRequest") String patientRequestJson) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			PatientRegistrationRequestDto patientRequest = objectMapper.readValue(patientRequestJson,
					PatientRegistrationRequestDto.class);

			responseDTO = patientService.mobPatientRegistration(patientRequest, profileImage);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Exception Occurred");
			responseDTO.setStatus(false);
		}
		return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
	}

	@PostMapping("/uploaddocument")
	public ResponseEntity<ResponseDTO> uploadPatientDocument(@Valid @ModelAttribute DocumentRequest request) {

		ResponseDTO responseDTO = new ResponseDTO();

		try {
			responseDTO = patientService.uploadPatientDocument(request);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Exception Occurred");
			responseDTO.setStatus(false);
		}
		return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));

	}

	@ApiOperation(value = "Fetch All Patients with optional filters")
	@GetMapping("/getAllPatients")
	public ResponseEntity<ResponseDTO> getAllPatients(@RequestParam(required = false) String mrdNo,
			@RequestParam(required = false) String patientName, @RequestParam(required = false) String email,
			@RequestParam(required = false) String phoneNo, @RequestParam(required = false) String nationalId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			Page<Patient> patientsPage = patientService.getPatientsWithFilters(mrdNo, patientName, email, phoneNo,
					nationalId, page, size);
			if (patientsPage.isEmpty()) {
				responseDTO.setStatusCode(404);
				responseDTO.setMessage("No patients found");
				responseDTO.setStatus(false);
				responseDTO.setData(new ArrayList<>());
			} else {
				responseDTO.setStatusCode(200);
				responseDTO.setMessage("Patients fetched successfully");
				responseDTO.setStatus(true);
				responseDTO.setData(patientsPage.getContent());
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Exception Occurred: " + e.getMessage());
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
		}

		return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
	}

	@ApiOperation(value = "searchPatients All Patients with optional filters")
	@GetMapping("/searchPatients")
	public ResponseEntity<ResponseDTO> searchPatients(@RequestParam(required = false) String searchKey,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO = patientService.searchPatients(searchKey, page, size);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Exception Occurred: " + e.getMessage());
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
		}

		return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
	}

}
