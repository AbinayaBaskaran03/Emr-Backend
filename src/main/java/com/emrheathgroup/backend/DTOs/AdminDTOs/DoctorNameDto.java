package com.emrheathgroup.backend.DTOs.AdminDTOs;

import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorNameDto {

    private String docName;
    private String specialization;
    private Time startTime;
    private Time endTime;
    private Integer docId;
    private Integer branchId;


}