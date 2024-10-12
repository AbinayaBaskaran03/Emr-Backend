package com.emrheathgroup.backend.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.emrheathgroup.backend.Enumeration.TimeInterval;

@Component
public interface TimeSlotService {

	List<String> generateTimeSlot(TimeInterval interval);

	List<String> getAvailableSlotsByDoctorID(TimeInterval interval, Integer doctorId, Date date);

	Map<Integer, List<String>> getAvailableSlotsForAllDoctors(TimeInterval interval, Date date);

	Map<Integer, List<String>> getAvailableSlotsForAllFacilities(TimeInterval interval, Date date);

	List<String> getAvailableSlotsByFacilityID(TimeInterval interval, Integer facilityId, Date date);

}
