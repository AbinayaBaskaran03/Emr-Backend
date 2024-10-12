package com.emrheathgroup.backend.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emrheathgroup.backend.Service.Security.SecurityService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("security")
public class SecurityController {

	@GetMapping("/refreshItemkeys")
	public ResponseEntity<String> refreshItemkeys() {
		try {
			SecurityService.refreshItemKeys();
			return new ResponseEntity<>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
