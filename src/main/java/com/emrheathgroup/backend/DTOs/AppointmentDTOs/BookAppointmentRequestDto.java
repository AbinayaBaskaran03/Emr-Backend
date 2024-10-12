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
public class BookAppointmentRequestDto {
	
	private Integer branchId;
    private Date appointmentDate;
    
    private Integer specialityId;
    
    private Integer doctorId;
    private Time appointmentTime;
    private String scheduleType;
    private String mrdNo;
    private String patientName;
    private Date dob;
    private Integer age;
    private Gender gender;
    private String phoneNo;
    private String email;
    private String notes;
    private String insuranceName;  
    private String appointmentStatus;

}
