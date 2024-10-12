package com.emrheathgroup.backend.Controller.Doctor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emrheathgroup.backend.Service.Doctor.DoctorService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("doctor")
public class DoctorDashboardController {

	@Autowired
	DoctorService doctorService;

	@GetMapping("/getCount")
	public ResponseEntity<List<Map<String, Object>>> getCountForDoctorDashboardData(@RequestParam int doctorId,
			@RequestParam int ipOpFlag) {
		List<Map<String, Object>> doctorCountData = null;
		try {
			doctorCountData = doctorService.getCountForDoctorDashboardData(doctorId);
			return new ResponseEntity<>(doctorCountData, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(doctorCountData, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getDataList")
	public ResponseEntity<List<Map<String, Object>>> getDataList(@RequestParam int doctorId, @RequestParam int ipOpFlag,
			@RequestParam String context, @RequestParam int page, @RequestParam int size, String ptName,
			String status) {
		try {
			List<Map<String, Object>> dataList = doctorService.getDataList(doctorId, ipOpFlag, ptName, status, context,
					page, size);
			return new ResponseEntity<>(dataList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
