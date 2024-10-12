package com.emrheathgroup.backend.DTOs.PatientDTOs;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentRequest {

	private Integer patientId;
	private String docNationalId;
	private String docType;
	private String ifOtherType;
	private MultipartFile document;

}
