package com.emrheathgroup.backend.DTOs.DoctorDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {
	
	
    private Integer doctorId;
	private String docName;
   // private Integer specialityId; 
	private String contactInfo;
	private Integer experience;

}
