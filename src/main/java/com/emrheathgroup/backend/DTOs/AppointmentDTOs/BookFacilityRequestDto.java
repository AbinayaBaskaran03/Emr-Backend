package com.emrheathgroup.backend.DTOs.AppointmentDTOs;

import java.sql.Time;
import java.util.Date;

import com.emrheathgroup.backend.Enumeration.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookFacilityRequestDto {

	
	private Integer branchId;
	private Integer rosterId;
    private Date scheduleDate;
    private Integer doctorId;
    
    private Time scheduleTime;
    private Time endTime; // Add this field if you want to input it manually

    private String scheduleType;
    private String mrdNo;
    private String patientName;
    private Date dob;
    private Integer age;
    private Gender gender;
    private String mobile;
    private String email;
    private String notes;
    private String insuranceName;  
}
