package com.emrheathgroup.backend.DTOs.PatientDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DependentRequest {

	private String depNationalId;
	private String name;
	private Integer age;
	private String relation;

  }
