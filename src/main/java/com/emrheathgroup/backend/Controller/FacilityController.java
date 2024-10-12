package com.emrheathgroup.backend.Controller;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.AppointmentDTOs.BookFacilityRequestDto;
import com.emrheathgroup.backend.DTOs.DoctorDTOs.BlockAvailabilityRequestDto;
import com.emrheathgroup.backend.Entity.FacilityScheduling;
import com.emrheathgroup.backend.Helper.StaticConstants;
import com.emrheathgroup.backend.Service.FacilityService;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/facility")
public class FacilityController {  
	
	@Autowired
	private FacilityService facilityService;
	
	@ApiOperation(value = " Book facility by receptionalist")
	@PostMapping("/bookFacility")
	public ResponseEntity<ResponseDTO> bookFacility(
			@RequestBody BookFacilityRequestDto bookFacilityRequestDto) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO = facilityService.bookFacility(bookFacilityRequestDto);

		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Exception Occured");
			responseDTO.setStatus(false);
		} finally {
			return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
		}
	}

	@ApiOperation(value = "Fetch All Facilities by Branch and Date")
	@GetMapping("/getAllFacilitiesByBranchAndDate")
	public ResponseEntity<ResponseDTO> getAllFacilitiesByBranchAndDate(@RequestParam Integer branchId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
	    ResponseDTO responseDTO = new ResponseDTO();
	    try {
	        responseDTO = facilityService.getAllFacilitiesByBranchAndDate(branchId, date);
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
	
	 @ApiOperation(value = "Get Facility Schedules with Filters")
	    @GetMapping("/getAllFacilitySchedules")
	    public ResponseEntity<ResponseDTO> getAllFacilitySchedules(
	            @RequestParam(required = false) String facilityName,
	            @RequestParam(required = false) String scheduleDate,
	            @RequestParam(required = false) String status,
	            @RequestParam(required = false) String patientName,
	            @RequestParam(required = false) String mrdNo,
	            @RequestParam(required = false) String email,
	            @RequestParam(required = false) String mobile,
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {

	        ResponseDTO responseDTO = facilityService.getFacilitySchedulesWithFilters(
	                facilityName, scheduleDate, status, patientName, mrdNo, email, mobile, page, size);
	        return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
	    }
	
	@ApiOperation(value = "Get Facility Schedule by Id")
	@GetMapping("/getFacilityScheduleById/{id}")
	public ResponseEntity<ResponseDTO> getFacilitySchedulesById(@PathVariable Integer id) {
	    ResponseDTO responseDTO;
	    try {
	        responseDTO = facilityService.getFacilitySchedulesById(id);
	    } catch (Exception e) {
	        e.printStackTrace();
	        responseDTO = new ResponseDTO();
	        responseDTO.setStatusCode(500);
	        responseDTO.setData(new ArrayList<>());
	        responseDTO.setMessage("Exception Occurred");
	        responseDTO.setStatus(false);
	    }

	    HttpStatus status = StaticConstants.statusCodes.getOrDefault(responseDTO.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
	    return new ResponseEntity<>(responseDTO, status);
	}
	
	
	  @ApiOperation(value = "Block the facility availability")
	    @PostMapping("/blockAvailability")
	    public ResponseEntity<ResponseDTO> blockFacilityAvailability(
	            @RequestBody BlockAvailabilityRequestDto blockAvailabilityRequestDto) {
	        ResponseDTO responseDTO = new ResponseDTO();
	        try {
	            responseDTO = facilityService.blockFacilityAvailability(blockAvailabilityRequestDto);
	        } catch (Exception e) {
	            responseDTO.setStatusCode(500);
	            responseDTO.setData(new ArrayList<>());
	            responseDTO.setMessage("Exception Occurred");
	            responseDTO.setStatus(false);
	        }
	        return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
	    }

	  @PostMapping("/generateFacilityRoster")
	    public ResponseEntity<ResponseDTO> createFacilityRoster(@RequestParam Integer branchId, 
	                                                            @RequestParam String facilityName, 
	                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
	                                                            @RequestParam Integer days) {
	        ResponseDTO responseDTO = new ResponseDTO();
	        try {
	        	responseDTO =  facilityService.generateFacilityRoster(branchId, facilityName, startDate, days);

	            responseDTO.setStatus(true);
	            responseDTO.setMessage("Facility Roster created for the next " + days + " days");
	            responseDTO.setStatusCode(201); 
	        } catch (Exception e) {
	            e.printStackTrace();
	            responseDTO.setStatus(false);
	            responseDTO.setMessage("Exception occurred while creating facility roster: " + e.getMessage());
	            responseDTO.setStatusCode(500); 
	        }
	        return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
	    }

}
