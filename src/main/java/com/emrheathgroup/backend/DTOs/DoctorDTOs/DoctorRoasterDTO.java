package com.emrheathgroup.backend.DTOs.DoctorDTOs;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRoasterDTO {
	
	    private Integer doctorId;
	    private Integer branchId;
	    private Date date;
	    private String startTime;
	    private String endTime;
	    
}
