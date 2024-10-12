package com.emrheathgroup.backend.ServiceImpl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emrheathgroup.backend.Entity.Doctor;
import com.emrheathgroup.backend.Entity.DoctorRoster;
import com.emrheathgroup.backend.Entity.FacilityRoster;
import com.emrheathgroup.backend.Enumeration.TimeInterval;
import com.emrheathgroup.backend.Repository.DoctorRepo;
import com.emrheathgroup.backend.Repository.DoctorRosterRepo;
import com.emrheathgroup.backend.Repository.FacilityRosterRepo;
import com.emrheathgroup.backend.Service.TimeSlotService;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

	private static final Logger log = LoggerFactory.getLogger(TimeSlotServiceImpl.class);

	private static final LocalTime FIXED_START_TIME = LocalTime.of(0, 0);
	private static final LocalTime FIXED_END_TIME = LocalTime.of(23, 0);

	@Autowired
	private DoctorRosterRepo doctorRosterRepo;

	@Autowired
	private DoctorRepo doctorRepo;
	
	@Autowired
	private FacilityRosterRepo facilityRosterRepo;

	@Override
	public List<String> generateTimeSlot(TimeInterval interval) {
		return Stream.iterate(FIXED_START_TIME, time -> time.plusMinutes(interval.getMinutes()))
				.limit(calculateSlotCount(interval)).takeWhile(time -> !time.isAfter(FIXED_END_TIME))
				.map(LocalTime::toString).collect(Collectors.toList());
	}

	private long calculateSlotCount(TimeInterval interval) {
		return (FIXED_END_TIME.toSecondOfDay() - FIXED_START_TIME.toSecondOfDay()) / (interval.getMinutes() * 60);
	}

	@Override
	public Map<Integer, List<String>> getAvailableSlotsForAllDoctors(TimeInterval interval, Date date) {
		try {
			List<DoctorRoster> rosters = doctorRosterRepo.findByDate(date);

			System.out.println("Fetched Rosters: " + rosters);

			if (rosters == null || rosters.isEmpty()) {
				System.out.println("No rosters found for the given date.");
				return new HashMap<>();
			}

			return rosters.stream().collect(
					Collectors.groupingBy(roster -> roster.getDoctor().getDoctorId(), Collectors.flatMapping(roster -> {
						List<String> slots = generateSlotsForRoster(interval, roster);
						if (slots.isEmpty()) {
							System.out.println("No slots generated for Doctor ID: " + roster.getDoctor().getDoctorId());
						}
						return slots.stream();
					}, Collectors.toList())));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error occurred while fetching available slots: " + e.getMessage());
			return new HashMap<>();
		}
	}

	private List<String> generateSlotsForRoster(TimeInterval interval, DoctorRoster roster) {
		try {
			LocalTime startTime = roster.getStartTime() != null ? roster.getStartTime().toLocalTime() : null;
			LocalTime endTime = roster.getEndTime() != null ? roster.getEndTime().toLocalTime() : null;

			if (startTime == null || endTime == null) {
				System.err.println("Start or End time is null for roster ID: " + roster.getId() + ", Doctor ID: "
						+ roster.getDoctor().getDoctorId());
				return new ArrayList<>();
			}

			if (endTime.isBefore(startTime)) {
				endTime = endTime.plusHours(24);
			}

			final LocalTime adjustedEndTime = endTime.isAfter(FIXED_END_TIME) ? FIXED_END_TIME : endTime;

			List<String> slots = Stream.iterate(startTime, time -> time.plusMinutes(interval.getMinutes()))
					.takeWhile(time -> !time.isAfter(adjustedEndTime)).map(LocalTime::toString)
					.collect(Collectors.toList());

			if (slots.isEmpty()) {
				System.out.println("No slots available for Doctor ID: " + roster.getDoctor().getDoctorId());
			}

			return slots;

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public List<String> getAvailableSlotsByDoctorID(TimeInterval interval, Integer doctorId, Date date) {
		Optional<Doctor> doctorOpt = doctorRepo.findById(doctorId);

		if (doctorOpt.isPresent()) {
			Doctor doctor = doctorOpt.get();
			Optional<DoctorRoster> rosterOpt = doctorRosterRepo.findByDoctorAndDate(doctor, date);

			return rosterOpt.map(roster -> generateSlotsForRoster(interval, roster)).orElseGet(List::of);
		} else {
			return List.of();
		}
	}

	@Override
	public Map<Integer, List<String>> getAvailableSlotsForAllFacilities(TimeInterval interval, Date date) {
	    try {
	        List<FacilityRoster> rosters = facilityRosterRepo.findByDate(date);

	        if (rosters == null || rosters.isEmpty()) {
	            return new HashMap<>();
	        }

	        return rosters.stream().collect(
	                Collectors.groupingBy(roster -> roster.getRosterId(), Collectors.flatMapping(roster -> {
	                    List<String> slots = generateSlotsForRoster(interval, roster);
	                    return slots.stream();
	                }, Collectors.toList())));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new HashMap<>();
	    }
	}

	private List<String> generateSlotsForRoster(TimeInterval interval, FacilityRoster roster) {
	    try {
	        LocalTime startTime = roster.getStartTime() != null ? roster.getStartTime().toLocalTime() : null;
	        LocalTime endTime = roster.getEndTime() != null ? roster.getEndTime().toLocalTime() : null;

	        if (startTime == null || endTime == null) {
	            return new ArrayList<>();
	        }

	        if (endTime.isBefore(startTime)) {
	            endTime = endTime.plusHours(24);
	        }

	        final LocalTime adjustedEndTime = endTime.isAfter(FIXED_END_TIME) ? FIXED_END_TIME : endTime;

	        List<String> slots = Stream.iterate(startTime, time -> time.plusMinutes(interval.getMinutes()))
	                .takeWhile(time -> !time.isAfter(adjustedEndTime)).map(LocalTime::toString)
	                .collect(Collectors.toList());

	        return slots;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}

	@Override
	public List<String> getAvailableSlotsByFacilityID(TimeInterval interval, Integer facilityId, Date date) {
	    Optional<FacilityRoster> rosterOpt = facilityRosterRepo.findByRosterIdAndDate (facilityId, date);

	    return rosterOpt.map(roster -> generateSlotsForRoster(interval, roster)).orElseGet(List::of);
	}

}
