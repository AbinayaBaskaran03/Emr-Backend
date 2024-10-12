package com.emrheathgroup.backend.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.Enumeration.TimeInterval;
import com.emrheathgroup.backend.Helper.StaticConstants;
import com.emrheathgroup.backend.Service.TimeSlotService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/timeslot")
public class TimeSlotController {

	@Autowired
	private TimeSlotService timeSlotService;

	public TimeSlotController(TimeSlotService timeSlotService) {
		this.timeSlotService = timeSlotService;
	}

	@GetMapping("/generateTimeSlot")
	public ResponseEntity<ResponseDTO> getTimeSlots(@RequestParam TimeInterval interval) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			List<String> timeSlots = timeSlotService.generateTimeSlot(interval);
			responseDTO.setStatusCode(200);
			responseDTO.setData(timeSlots);
			responseDTO.setMessage("Time slots generated successfully");
			responseDTO.setStatus(true);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Exception Occurred");
			responseDTO.setStatus(false);
		} finally {
			return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
		}
	}

	@GetMapping("/getAvailableSlotsForAllDoctors")
	public ResponseEntity<ResponseDTO> getAvailableSlotsForAllDoctors(@RequestParam TimeInterval interval,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

		ResponseDTO responseDTO = new ResponseDTO();
		try {
			Map<Integer, List<String>> doctorSlotsMap = timeSlotService.getAvailableSlotsForAllDoctors(interval, date);

			responseDTO.setStatusCode(200);
			responseDTO.setData(doctorSlotsMap);
			responseDTO.setMessage("Available slots fetched successfully for all doctors");
			responseDTO.setStatus(true);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Exception Occurred");
			responseDTO.setStatus(false);
		} finally {
			return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
		}
	}

	@GetMapping("/getAvailableSlotsByDoctorID")
	public ResponseEntity<ResponseDTO> getAvailableSlotsByDoctorID(@RequestParam TimeInterval interval,
			@RequestParam Integer doctorId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			List<String> availableSlots = timeSlotService.getAvailableSlotsByDoctorID(interval, doctorId, date);
			responseDTO.setStatusCode(200);
			responseDTO.setData(availableSlots);
			responseDTO.setMessage("Available slots fetched successfully for the doctor");
			responseDTO.setStatus(true);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Exception Occurred");
			responseDTO.setStatus(false);
		} finally {
			return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
		}
	}
	
	
	@GetMapping("/getAvailableSlotsForAllFacilities")
	public ResponseEntity<ResponseDTO> getAvailableSlotsForAllFacilities(@RequestParam TimeInterval interval,
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

	    ResponseDTO responseDTO = new ResponseDTO();
	    try {
	        Map<Integer, List<String>> facilitySlotsMap = timeSlotService.getAvailableSlotsForAllFacilities(interval, date);

	        responseDTO.setStatusCode(200);
	        responseDTO.setData(facilitySlotsMap);
	        responseDTO.setMessage("Available slots fetched successfully for all facilities");
	        responseDTO.setStatus(true);
	    } catch (Exception e) {
	        e.printStackTrace();
	        responseDTO.setStatusCode(500);
	        responseDTO.setData(new ArrayList<>());
	        responseDTO.setMessage("Exception Occurred");
	        responseDTO.setStatus(false);
	    } finally {
	        return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
	    }
	}

	@GetMapping("/getAvailableSlotsByFacilityID")
	public ResponseEntity<ResponseDTO> getAvailableSlotsByFacilityID(@RequestParam TimeInterval interval,
	        @RequestParam Integer facilityId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
	    ResponseDTO responseDTO = new ResponseDTO();
	    try {
	        List<String> availableSlots = timeSlotService.getAvailableSlotsByFacilityID(interval, facilityId, date);
	        responseDTO.setStatusCode(200);
	        responseDTO.setData(availableSlots);
	        responseDTO.setMessage("Available slots fetched successfully for the facility");
	        responseDTO.setStatus(true);
	    } catch (Exception e) {
	        e.printStackTrace();
	        responseDTO.setStatusCode(500);
	        responseDTO.setData(new ArrayList<>());
	        responseDTO.setMessage("Exception Occurred");
	        responseDTO.setStatus(false);
	    } finally {
	        return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
	    }
	}

	
}
