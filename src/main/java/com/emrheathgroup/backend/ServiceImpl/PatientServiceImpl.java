package com.emrheathgroup.backend.ServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.PatientDTOs.DocumentRequest;
import com.emrheathgroup.backend.DTOs.PatientDTOs.PatientRegistrationRequestDto;
import com.emrheathgroup.backend.Entity.Patient;
import com.emrheathgroup.backend.Entity.PatientDocument;
import com.emrheathgroup.backend.Enumeration.Status;
import com.emrheathgroup.backend.Repository.PatientDependentRepo;
import com.emrheathgroup.backend.Repository.PatientDocumentRepo;
import com.emrheathgroup.backend.Repository.PatientRepo;
import com.emrheathgroup.backend.Service.PatientService;

import jakarta.persistence.criteria.Predicate;

@Service
public class PatientServiceImpl implements PatientService {

	private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

	@Autowired
	private PatientRepo patientRepo;

	@Autowired
	private PatientDependentRepo patientDependentRepo;

	@Autowired
	private PatientDocumentRepo patientDocumentRepo;

	@Override
	public ResponseDTO getPatientByNationalId(String nationalId) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			Patient patient = patientRepo.findByNationalId(nationalId).orElse(null);
			if (patient != null) {
				responseDTO.setStatusCode(201);
				responseDTO.setData(patient);
				responseDTO.setMessage("Patient Fetched Successfully");
				responseDTO.setStatus(true);
				return responseDTO;
			}
			responseDTO.setStatusCode(404);
			responseDTO.setMessage("Patient Not Found for given national Id");
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatus(false);
			return responseDTO;

		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Execution Occurred");

		} finally {
			return responseDTO;

		}
	}

	@Override
	public ResponseDTO getPatientByMrdNo(String mrdNo) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			Patient patient = patientRepo.findByMrdNo(mrdNo).orElse(null);
			if (patient != null) {
				responseDTO.setStatusCode(200);
				responseDTO.setData(patient);
				responseDTO.setMessage("Patient Fetched Successfully");
				responseDTO.setStatus(true);
				return responseDTO;
			}
			responseDTO.setStatusCode(404);
			responseDTO.setMessage("Patient Not Found for given MRD number");
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatus(false);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Execution Occurred");
		} finally {
			return responseDTO;
		}
	}

	@Override
	public ResponseDTO getPatientByEmailId(String email) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			Patient patient = patientRepo.findByEmailId(email).orElse(null);
			if (patient != null) {
				responseDTO.setStatusCode(200);
				responseDTO.setData(patient);
				responseDTO.setMessage("Patient Fetched Successfully");
				responseDTO.setStatus(true);
				return responseDTO;
			}
			responseDTO.setStatusCode(404);
			responseDTO.setMessage("Patient Not Found for given emailId");
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatus(false);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Execution Occurred");
		} finally {
			return responseDTO;
		}
	}

	@Override
	public ResponseDTO getPatientByPhoneNo(String phoneNo) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			Patient patient = patientRepo.findByPhoneNo(phoneNo).orElse(null);
			if (patient != null) {
				responseDTO.setStatusCode(200);
				responseDTO.setData(patient);
				responseDTO.setMessage("Patient Fetched Successfully");
				responseDTO.setStatus(true);
				return responseDTO;
			}
			responseDTO.setStatusCode(404);
			responseDTO.setMessage("Patient Not Found for given phoneNo");
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatus(false);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Execution Occurred");
		} finally {
			return responseDTO;
		}
	}

	@Override
	public ResponseDTO getPatientByName(String name) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			Patient patient = patientRepo.findByName(name).orElse(null);
			if (patient != null) {
				responseDTO.setStatusCode(200);
				responseDTO.setData(patient);
				responseDTO.setMessage("Patient Fetched Successfully");
				responseDTO.setStatus(true);
				return responseDTO;
			}
			responseDTO.setStatusCode(404);
			responseDTO.setMessage("Patient Not Found for given phoneNo");
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatus(false);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Execution Occurred");
		} finally {
			return responseDTO;
		}
	}

	@Override
	public Page<Patient> getPatientsWithFilters(String mrdNo, String patientName, String email, String phoneNo,
			String nationalId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		return patientRepo.findAll((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (mrdNo != null && !mrdNo.isEmpty()) {
				predicates.add(cb.equal(root.get("mrdNo"), mrdNo));
			}
			if (patientName != null && !patientName.isEmpty()) {
				predicates.add(cb.like(root.get("name"), "%" + patientName + "%"));
			}
			if (email != null && !email.isEmpty()) {
				predicates.add(cb.equal(root.get("emailId"), email));
			}
			if (phoneNo != null && !phoneNo.isEmpty()) {
				predicates.add(cb.equal(root.get("phoneNo"), phoneNo));
			}
			if (nationalId != null && !nationalId.isEmpty()) {
				predicates.add(cb.equal(root.get("nationalId"), nationalId));
			}

			query.orderBy(cb.asc(root.get("name")));
			return cb.and(predicates.toArray(new Predicate[0]));
		}, pageable);
	}

	@Override
	public ResponseDTO mobPatientRegistration(PatientRegistrationRequestDto patientRequest,
			MultipartFile profileImage) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			Optional<Patient> existingPatientOpt = patientRepo.findByNationalId(patientRequest.getNationalId());

			if (existingPatientOpt.isPresent()) {
				responseDTO.setStatusCode(200);
				responseDTO.setMessage("Patient already exists with this National ID.");
				responseDTO.setData(existingPatientOpt.get());
				responseDTO.setStatus(false);
				return responseDTO;
			}

			Optional<Patient> existingEmailPatientOpt = patientRepo.findByEmailId(patientRequest.getEmailId());
			if (existingEmailPatientOpt.isPresent()) {
				responseDTO.setStatusCode(409);
				responseDTO.setMessage("A patient with this email already exists.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			Patient existingOtherIdPatient = patientRepo.findByOtherId(patientRequest.getOtherId());
			if (existingOtherIdPatient != null) {
				responseDTO.setStatusCode(409);
				responseDTO.setMessage("A patient with this Other ID already exists.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			String newMrdNo = generateMRDNumber();
			Patient newPatient = new Patient();
			newPatient.setMrdNo(newMrdNo);
			newPatient.setNationalId(patientRequest.getNationalId());
			newPatient.setName(patientRequest.getName());
			newPatient.setDob(patientRequest.getDob());
			newPatient.setAge(patientRequest.getAge());
			newPatient.setGender(patientRequest.getGender());
			newPatient.setEmailId(patientRequest.getEmailId());
			newPatient.setNationality(patientRequest.getNationality());
			newPatient.setMaritalStatus(patientRequest.getMaritalStatus().toString());
			newPatient.setVisaType(patientRequest.getVisaType());
			newPatient.setOtherId(patientRequest.getOtherId());
			newPatient.setOccupation(patientRequest.getOccupation());
			newPatient.setAddress(patientRequest.getAddress());
			newPatient.setPhoneNo(patientRequest.getPhoneNo());
			newPatient.setProblem(patientRequest.getProblem());
			newPatient.setSymptoms(patientRequest.getSymptoms());
			newPatient.setStatus(Status.Active);
			if (profileImage != null && !profileImage.isEmpty()) {
				long fileSizeInKB = profileImage.getSize() / 1024;
				if (fileSizeInKB > 500) {
					responseDTO.setStatusCode(400);
					responseDTO.setMessage("Profile image must be under 500 KB.");
					responseDTO.setStatus(false);
					return responseDTO;
				}

				if (isValidImage(profileImage)) {
					newPatient.setProfile(profileImage.getBytes());
				} else {
					responseDTO.setStatusCode(400);
					responseDTO.setMessage("Invalid profile image. Only JPG and PNG formats are allowed.");
					responseDTO.setStatus(false);
					return responseDTO;
				}
			}

			Patient savedPatient = patientRepo.save(newPatient);
			responseDTO.setStatusCode(200);
			responseDTO.setData(savedPatient);
			responseDTO.setMessage("Patient registered successfully. MRD No: " + newMrdNo);
			responseDTO.setStatus(true);

		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Error occurred during patient registration.");
			responseDTO.setStatus(false);
			responseDTO.setData(null);
		}

		return responseDTO;
	}

	private boolean isValidImage(MultipartFile file) {
		String contentType = file.getContentType();
		return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png"));
	}

	private String generateMRDNumber() {
		Random random = new Random();
		String mrdNo;
		boolean exists;

		do {
			int randomNumber = random.nextInt(100000);
			mrdNo = "MRD" + randomNumber;
			exists = patientRepo.existsByMrdNo(mrdNo);
		} while (exists);

		return mrdNo;
	}

	@Override
	public ResponseDTO uploadPatientDocument(DocumentRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			Patient patient = patientRepo.findById(request.getPatientId()).orElse(null);
			if (patient == null) {
				responseDTO.setStatusCode(404);
				responseDTO.setMessage("Patient not found");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			MultipartFile documentFile = request.getDocument();
			if (documentFile.isEmpty() || documentFile.getSize() > 500 * 1024) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("Document file must not be empty and must be under 500 KB.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			String contentType = documentFile.getContentType();
			if (!isValidDocumentType(contentType)) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("Invalid document type. Allowed types are: Word, PDF, and images.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			PatientDocument document = new PatientDocument();
			document.setPatient(patient);
			document.setDocNationalId(request.getDocNationalId());
			document.setDocType(request.getDocType());
			document.setIfOtherType(request.getIfOtherType());
			document.setDocument(documentFile.getBytes());

			patientDocumentRepo.save(document);

			responseDTO.setStatusCode(200);
			responseDTO.setMessage("Document uploaded successfully");
			responseDTO.setData(document);
			responseDTO.setStatus(true);

		} catch (IOException e) {
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Failed to upload document due to an internal error");
			responseDTO.setStatus(false);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("An unexpected error occurred");
			responseDTO.setStatus(false);
		}

		return responseDTO;
	}

	private boolean isValidDocumentType(String contentType) {
		return contentType != null && (contentType.equals("application/pdf") || contentType.equals("application/msword")
				|| contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
				|| contentType.startsWith("image/jpeg"));
	}

	@Override
	public ResponseDTO searchPatients(String searchKey, int page, int size) {
		ResponseDTO responseDTO = new ResponseDTO();
		Pageable pageable = PageRequest.of(page, size);

		Page<Patient> patientsPage;

		if (searchKey == null || searchKey.trim().isEmpty()) {
			patientsPage = patientRepo.findAll(pageable);
		} else {
			patientsPage = patientRepo.findByAnyField(searchKey, pageable);
		}

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

		return responseDTO;
	}
}
