package com.emrheathgroup.backend.Controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.AdminDTOs.DoctorAppointmentDto;
import com.emrheathgroup.backend.DTOs.AppointmentDTOs.AppointmentRescheduleDto;
import com.emrheathgroup.backend.DTOs.AppointmentDTOs.BookAppointmentRequestDto;
import com.emrheathgroup.backend.DTOs.AppointmentDTOs.CancelAppointmentDto;
import com.emrheathgroup.backend.Entity.DoctorAppointment;
import com.emrheathgroup.backend.Helper.StaticConstants;
import com.emrheathgroup.backend.Repository.DoctorAppointmentRepo;
import com.emrheathgroup.backend.Service.DoctorAppointmentService;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/doctorappointment")
public class DoctorAppointmentController {

	@Autowired
	private DoctorAppointmentService doctorAppointmentService;

	@Autowired
	private DoctorAppointmentRepo doctorAppointmentRepo;

	@ApiOperation(value = "Mob Book Appointment by Receptionalist")
	@PostMapping("/mobBookAppointment")
	public ResponseEntity<ResponseDTO> MobBookAppointmentByReceptionalist(
			@RequestBody DoctorAppointmentDto appointmentDto) {

		ResponseDTO responseDTO = new ResponseDTO();

		try {
			responseDTO = doctorAppointmentService.MobBookAppointmentByReceptionalist(appointmentDto);

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

	@ApiOperation(value = "Web Book Appointment by receptionalist")
	@PostMapping("/bookAppointment")
	public ResponseEntity<ResponseDTO> WebBookAppointmentByReceptionalist(
			@RequestBody BookAppointmentRequestDto requestDto) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO = doctorAppointmentService.webBookAppointment(requestDto);

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
 
	@ApiOperation(value = "Fetch All Appointments with optional filters")
	@GetMapping("/getAllAppointments")
	public ResponseEntity<ResponseDTO> getAllAppointments(@RequestParam(required = false) String appointmentDate,
			@RequestParam(required = false) String mrdNo, @RequestParam(required = false) String nationalId,
			@RequestParam(required = false) String mobile, @RequestParam(required = false) String patientName,
			@RequestParam(required = false) String doctorName, @RequestParam(required = false) String status,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			Page<DoctorAppointment> appointments = doctorAppointmentService.getAppointmentsWithFilters(appointmentDate,
					mrdNo, nationalId, mobile, patientName, doctorName, status, page, size);
			if (appointments.isEmpty()) {
				responseDTO.setStatusCode(404);
				responseDTO.setMessage("No appointments found");
				responseDTO.setStatus(false);
				responseDTO.setData(new ArrayList<>());
			} else {
				responseDTO.setStatusCode(200);
				responseDTO.setMessage("Appointments fetched successfully");
				responseDTO.setStatus(true);
				responseDTO.setData(appointments);
    
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Exception Occurred: " + e.getMessage());
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
		}
		return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
	}

	@ApiOperation(value = "Fetch Appointments by Doctor ID")
	@GetMapping("/getAppointmentsByDoctorID/{doctorId}")
	public ResponseEntity<ResponseDTO> getAppointmentsByDoctorID(@PathVariable Integer doctorId) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO = doctorAppointmentService.getAppointmentsByDoctorID(doctorId);
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
	
	@ApiOperation(value = "Fetch past,today,future Appointments  by Doctor ID")
	@GetMapping("/getAppointsByDocWithFilters")
	public ResponseEntity<ResponseDTO> getAppointsByDocWithFilters(@RequestParam Integer doctorId,@RequestParam String filters) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO = doctorAppointmentService.getAppointsByDocWithFilters(doctorId,filters);
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
 
	@ApiOperation(value = "Reschedule Appointments by ID")
	@PatchMapping("/rescheduleAppointment")
	public ResponseEntity<ResponseDTO> rescheduleAppointment(@RequestBody AppointmentRescheduleDto rescheduleDto) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO = doctorAppointmentService.rescheduleAppointment(rescheduleDto);
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

	@ApiOperation(value = "Cancel an existing appointment")
	@PostMapping("/cancelAppointment")
	public ResponseEntity<ResponseDTO> cancelAppointment(@RequestBody CancelAppointmentDto cancelAppointmentDto) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO = doctorAppointmentService.cancelAppointment(cancelAppointmentDto);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Exception occurred");
			responseDTO.setStatus(false);
		} finally {
			return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
		}
	}

	
}
