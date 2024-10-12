package com.emrheathgroup.backend.Service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.DoctorDTOs.BlockAvailabilityRequestDto;
import com.emrheathgroup.backend.DTOs.DoctorDTOs.DoctorRoasterDTO;
import com.emrheathgroup.backend.DTOs.TimeSlot.MobTimeSlotDto;

@Component
public interface DoctorService {

	ResponseDTO getAllSpecialities();

	ResponseDTO getAllDoctors();

	ResponseDTO getDoctorById(Integer id);

	ResponseDTO getDoctorsByBranch(Integer branchId);

	ResponseDTO blockAvailability(BlockAvailabilityRequestDto blockAvailabilityRequestDto);

	ResponseDTO doctorRosterCreation(DoctorRoasterDTO doctorRoasterDTO);

	ResponseDTO generateDoctorRoster(Integer doctorId, Integer branchId, Date startDate, Integer days);

	ResponseDTO getAvailableDoctors(Date date, Integer branchId, Integer specialityId);

	ResponseDTO getDoctorsBySpecialist(Integer specialityId);

	//List<MobTimeSlotDto> getAvailableTimeSlots(Integer doctorId, Date appointmentDate);

}
