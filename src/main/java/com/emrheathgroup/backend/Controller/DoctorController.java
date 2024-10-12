package com.emrheathgroup.backend.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.emrheathgroup.backend.DTOs.DoctorDTOs.BlockAvailabilityRequestDto;
import com.emrheathgroup.backend.DTOs.TimeSlot.MobTimeSlotDto;
import com.emrheathgroup.backend.Helper.StaticConstants;
import com.emrheathgroup.backend.Service.DoctorService;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/doctor")
public class DoctorController {

	@Autowired
	private DoctorService doctorService;

	@ApiOperation(value = "Fetch all specialities")
	@GetMapping("/getAllSpecialities") 
	public ResponseEntity<ResponseDTO> getAllSpecialities() {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			responseDTO = doctorService.getAllSpecialities();
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

	@ApiOperation(value = "Fetch all Doctors")
	@GetMapping("/getAllDoctors")
	public ResponseEntity<ResponseDTO> getAllDoctors() {

		ResponseDTO responseDTO = new ResponseDTO();

		try {
			responseDTO = doctorService.getAllDoctors();
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

	@ApiOperation(value = "Fetch  Doctors details By doctorId")
	@GetMapping("/getDoctorsById/{id}")
	public ResponseEntity<ResponseDTO> getDoctorById(@PathVariable Integer id) {

		ResponseDTO responseDTO = new ResponseDTO();

		try {
			responseDTO = doctorService.getDoctorById(id);
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

	@ApiOperation(value = "Fetch  Doctors details By branchId")
	@GetMapping("/getDoctorsByBranch/{branchId}")
	public ResponseEntity<ResponseDTO> getDoctorsByBranch(@PathVariable Integer branchId) {

		ResponseDTO responseDTO = new ResponseDTO();

		try {
			responseDTO = doctorService.getDoctorsByBranch(branchId);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setMessage("Exception Occured");
			responseDTO.setStatus(false);
		}
		return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
	}

	@ApiOperation(value = "Block the doctor availability")
	@PostMapping("/blockAvailability")
	public ResponseEntity<ResponseDTO> blockAvailability(
			@RequestBody BlockAvailabilityRequestDto blockAvailabilityRequestDto) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {

			responseDTO = doctorService.blockAvailability(blockAvailabilityRequestDto);

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

    @PostMapping("/generateDoctorRoster")
    public ResponseEntity<ResponseDTO> createDoctorRoster(@RequestParam Integer doctorId, 
                                                          @RequestParam Integer branchId, 
                                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                          @RequestParam Integer days) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
        	responseDTO = doctorService.generateDoctorRoster(doctorId, branchId, startDate, days);

        } catch (Exception e) {
            e.printStackTrace();
            responseDTO.setStatus(false);
            responseDTO.setMessage("Exception occurred while creating doctor roster");
            responseDTO.setStatusCode(500); 
        } finally {
            return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
        }
    }
	
    @ApiOperation(value = "Fetch Doctors by Specialist")
    @GetMapping("/getDoctorsBySpecialist")
    public ResponseEntity<ResponseDTO> getDoctorsBySpecialist(@RequestParam Integer specialityId) {

        ResponseDTO responseDTO = new ResponseDTO();

        try {
            responseDTO = doctorService.getDoctorsBySpecialist(specialityId);
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO.setStatusCode(500);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setMessage("Exception Occurred");
            responseDTO.setStatus(false);
        }

        return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
    }

    
    @GetMapping("/getAvailableDoctor")
    public ResponseEntity<ResponseDTO> getAvailableDoctors(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam("branchId") Integer branchId,
            @RequestParam("specialityId") Integer specialityId) {
    	  ResponseDTO responseDTO = new ResponseDTO();

          try {
              responseDTO = doctorService.getAvailableDoctors(date,branchId,specialityId);
          } catch (Exception e) {
              e.printStackTrace(); 
              responseDTO.setStatusCode(500);
              responseDTO.setData(new ArrayList<>());
              responseDTO.setMessage("Exception Occurred");
              responseDTO.setStatus(false);
          }

          return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
      }
    
   
}
