package com.emrheathgroup.backend.Service;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.AppointmentDTOs.BookFacilityRequestDto;
import com.emrheathgroup.backend.DTOs.DoctorDTOs.BlockAvailabilityRequestDto;

@Component
public interface FacilityService {

	ResponseDTO bookFacility(BookFacilityRequestDto bookFacilityRequestDto);

	ResponseDTO getFacilitySchedulesById(Integer id);

	ResponseDTO blockFacilityAvailability(BlockAvailabilityRequestDto blockAvailabilityRequestDto);

	ResponseDTO generateFacilityRoster(Integer branchId, String facilityName, Date startDate, Integer days);

	ResponseDTO getAllFacilitiesByBranchAndDate(Integer branchId, Date date);

	ResponseDTO getFacilitySchedulesWithFilters(String facilityName, String scheduleDate, String status,
			String patientName, String mrdNo, String email, String mobile, int page, int size);

}
