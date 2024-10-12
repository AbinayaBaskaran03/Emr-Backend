package com.emrheathgroup.backend.ServiceImpl;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.DoctorDTOs.BlockAvailabilityRequestDto;
import com.emrheathgroup.backend.DTOs.DoctorDTOs.DoctorRoasterDTO;
import com.emrheathgroup.backend.DTOs.TimeSlot.MobTimeSlotDto;
import com.emrheathgroup.backend.Entity.BranchMaster;
import com.emrheathgroup.backend.Entity.Doctor;
import com.emrheathgroup.backend.Entity.DoctorRoster;
import com.emrheathgroup.backend.Entity.DoctorSpecialityMapping;
import com.emrheathgroup.backend.Entity.Speciality;
import com.emrheathgroup.backend.Repository.BranchRepo;
import com.emrheathgroup.backend.Repository.DocSpecialityMapRepo;
import com.emrheathgroup.backend.Repository.DoctorRepo;
import com.emrheathgroup.backend.Repository.DoctorRosterRepo;
import com.emrheathgroup.backend.Repository.SpecialityRepo;
import com.emrheathgroup.backend.Service.DoctorService;

@Service
public class DoctorServiceImpl implements DoctorService {

	private static final Logger log = LoggerFactory.getLogger(DoctorServiceImpl.class);

	@Autowired
	DoctorRepo doctorRepo;

	@Autowired
	SpecialityRepo specialityRepo;

	@Autowired
	BranchRepo branchRepo;

	@Autowired
	DoctorRosterRepo webDoctorRosterRepo;

	@Autowired
	private DocSpecialityMapRepo docSpecialityMapRepo;

	@Override
	public ResponseDTO getAllSpecialities() {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			List<Speciality> branchMasters = specialityRepo.findAll();

			responseDTO.setStatus(true);
			responseDTO.setData(branchMasters);
			responseDTO.setMessage("Specialities Fetched Successfully");
			responseDTO.setStatusCode(200);

			log.info("All Specialities List Fetched");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception Occured: " + e);
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Exception Occured");
		} finally {
			return responseDTO;
		}
	}

	@Override
	public ResponseDTO getAllDoctors() {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			List<DoctorSpecialityMapping> doctorSpecialityMappings = docSpecialityMapRepo.findAll();

			List<Map<String, Object>> result = new ArrayList<>();

			for (DoctorSpecialityMapping mapping : doctorSpecialityMappings) {
				Map<String, Object> doctorData = new HashMap<>();
				doctorData.put("doctorId", mapping.getDoctor().getDoctorId());
				doctorData.put("doctorName", mapping.getDoctor().getName());
				doctorData.put("branchName", mapping.getDoctor().getBranchMaster().getBranchName());
				doctorData.put("experience", mapping.getDoctor().getExperience());
				doctorData.put("contactInfo", mapping.getDoctor().getContactInfo());
				doctorData.put("active", mapping.getDoctor().getActive());
				doctorData.put("specialityId", mapping.getSpeciality().getSpecialityId());
				doctorData.put("specialityName", mapping.getSpeciality().getName());

				result.add(doctorData);
			}

			responseDTO.setStatus(true);
			responseDTO.setData(result);
			responseDTO.setMessage("Doctors Fetched Successfully");
			responseDTO.setStatusCode(200);
			log.info("Doctors Fetched Successfully");
		} catch (Exception e) {
			log.error("Exception Occurred: ", e);
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Exception Occurred");
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO getDoctorById(Integer id) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			List<DoctorSpecialityMapping> doctorSpecialityMappings = docSpecialityMapRepo.findByDoctor_DoctorId(id);

			if (doctorSpecialityMappings.isEmpty()) {
				responseDTO.setStatus(false);
				responseDTO.setData(new ArrayList<>());
				responseDTO.setStatusCode(404);
				responseDTO.setMessage("Doctor ID not found");
				return responseDTO;
			}

			Doctor doctor = doctorSpecialityMappings.get(0).getDoctor();

			Map<String, Object> doctorData = new HashMap<>();
			doctorData.put("doctorId", doctor.getDoctorId());
			doctorData.put("doctorName", doctor.getName());
			doctorData.put("branchName", doctor.getBranchMaster().getBranchName());
			doctorData.put("experience", doctor.getExperience());
			doctorData.put("contactInfo", doctor.getContactInfo());
			doctorData.put("active", doctor.getActive());

			List<Map<String, Object>> specializations = new ArrayList<>();
			for (DoctorSpecialityMapping mapping : doctorSpecialityMappings) {
				Map<String, Object> specialityData = new HashMap<>();
				specialityData.put("specialityId", mapping.getSpeciality().getSpecialityId());
				specialityData.put("specialityName", mapping.getSpeciality().getName());
				specializations.add(specialityData);
			}

			doctorData.put("specializations", specializations);

			responseDTO.setStatus(true);
			responseDTO.setData(doctorData);
			responseDTO.setMessage("Doctor Fetched Successfully");
			responseDTO.setStatusCode(200);
			log.info("Doctor Fetched Successfully");

		} catch (Exception e) {
			log.error("Exception Occurred: ", e);
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Exception Occurred");
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO getDoctorsByBranch(Integer branchId) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			BranchMaster branchMaster = branchRepo.findById(branchId)
					.orElseThrow(() -> new RuntimeException("Branch not found"));

			List<Doctor> doctors = doctorRepo.findByBranchMaster(branchMaster);

			if (doctors.isEmpty()) {
				log.info("No doctors found for the given branchId");
				responseDTO.setStatus(false);
				responseDTO.setMessage("No doctors found for the given branchId");
				responseDTO.setData(null);
				responseDTO.setStatusCode(404);
			} else {
				List<Map<String, Object>> doctorList = new ArrayList<>();

				for (Doctor doctor : doctors) {
					Map<String, Object> doctorData = new HashMap<>();
					doctorData.put("doctorId", doctor.getDoctorId());
					doctorData.put("doctorName", doctor.getName());
					doctorData.put("experience", doctor.getExperience());
					doctorData.put("contactInfo", doctor.getContactInfo());
					doctorData.put("active", doctor.getActive());

					List<DoctorSpecialityMapping> doctorSpecialityMappings = docSpecialityMapRepo
							.findByDoctor_DoctorId(doctor.getDoctorId());

					List<Map<String, Object>> specializations = new ArrayList<>();
					for (DoctorSpecialityMapping mapping : doctorSpecialityMappings) {
						Map<String, Object> specialityData = new HashMap<>();
						specialityData.put("specialityId", mapping.getSpeciality().getSpecialityId());
						specialityData.put("specialityName", mapping.getSpeciality().getName());
						specializations.add(specialityData);
					}

					doctorData.put("specializations", specializations);
					doctorList.add(doctorData);
				}

				Map<String, Object> branchData = new HashMap<>();
				branchData.put("branchId", branchMaster.getBranchId());
				branchData.put("branchName", branchMaster.getBranchName());
				branchData.put("doctors", doctorList);

				responseDTO.setStatus(true);
				responseDTO.setData(branchData);
				responseDTO.setMessage("Doctors fetched successfully");
				responseDTO.setStatusCode(200);
				log.info("Doctors fetched successfully");
			}
		} catch (Exception e) {
			log.error("Exception occurred: ", e);
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Exception occurred");
		}

		return responseDTO;
	}

	@Override
	public ResponseDTO blockAvailability(BlockAvailabilityRequestDto blockAvailabilityRequestDto) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			Integer doctorId = blockAvailabilityRequestDto.getDoctorId();
			Integer branchId = blockAvailabilityRequestDto.getBranchId();
			Date requestDate = blockAvailabilityRequestDto.getDate();
			Time blockStartTime = blockAvailabilityRequestDto.getStartTime();
			Time blockEndTime = blockAvailabilityRequestDto.getEndTime();
			String title = blockAvailabilityRequestDto.getTitle();

			Optional<Doctor> doctorOpt = doctorRepo.findById(doctorId);
			Optional<BranchMaster> branchOpt = branchRepo.findById(branchId);

			if (!doctorOpt.isPresent() || !branchOpt.isPresent()) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("Invalid doctor or branch ID.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			Doctor doctor = doctorOpt.get();
			BranchMaster branch = branchOpt.get();

			java.sql.Date sqlDate = new java.sql.Date(requestDate.getTime());

			log.info("Blocking availability for doctor ID " + doctorId + " on date " + sqlDate);

			boolean isBlocked = blockDoctorTiming(doctor, branch, sqlDate, blockStartTime, blockEndTime, title);

			if (isBlocked) {
				responseDTO.setStatusCode(200);
				responseDTO.setMessage("Doctor's availability blocked successfully.");
				responseDTO.setStatus(true);
			} else {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("Failed to block doctor's availability.");
				responseDTO.setStatus(false);
			}

		} catch (Exception e) {
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("An error occurred: " + e.getMessage());
			responseDTO.setStatus(false);
			log.error("Error blocking availability: ", e);
		}

		return responseDTO;
	}

	private boolean blockDoctorTiming(Doctor doctor, BranchMaster branch, java.sql.Date date, Time startTime,
			Time endTime, String title) {
		return true;
	}

	@Override
	public ResponseDTO doctorRosterCreation(DoctorRoasterDTO doctorRoasterDTO) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			Optional<BranchMaster> branchMaster = branchRepo.findById(doctorRoasterDTO.getBranchId());
			if (branchMaster.isEmpty()) {
				log.info("Branch Id not found");
				responseDTO.setStatus(false);
				responseDTO.setMessage("BranchId Not Found");
				responseDTO.setData(new ArrayList<>());
				responseDTO.setStatusCode(404);
				return responseDTO;
			}

			Optional<Doctor> doctor = doctorRepo.findById(doctorRoasterDTO.getDoctorId());
			if (doctor.isEmpty()) {
				log.info("Doctor Id not found");
				responseDTO.setStatus(false);
				responseDTO.setMessage("DoctorId Not Found");
				responseDTO.setData(new ArrayList<>());
				responseDTO.setStatusCode(404);
				return responseDTO;
			}

			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

			Time startTime = new Time(sdf.parse(doctorRoasterDTO.getStartTime()).getTime());
			Time endTime = new Time(sdf.parse(doctorRoasterDTO.getEndTime()).getTime());

			DoctorRoster doctorRoster = new DoctorRoster();
			doctorRoster.setDoctor(doctor.get());
			doctorRoster.setBranchMaster(branchMaster.get());
			doctorRoster.setDate(doctorRoasterDTO.getDate());
			doctorRoster.setStartTime(startTime);
			doctorRoster.setEndTime(endTime);
			doctorRoster.setStatus(true);

			webDoctorRosterRepo.save(doctorRoster);

			responseDTO.setStatusCode(201);
			responseDTO.setMessage("DoctorRoster Created Successfully");
			responseDTO.setData(doctorRoster);
			responseDTO.setStatus(true);

			log.info("DoctorRoster Created Successfully");

		} catch (ParseException e) {
			e.printStackTrace();
			log.error("Time Parsing Exception Occurred: " + e);
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(400);
			responseDTO.setMessage("Invalid Time Format");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception Occurred: " + e);
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Exception Occurred");
		} finally {
			return responseDTO;
		}
	}

	@Override
	public ResponseDTO generateDoctorRoster(Integer doctorId, Integer branchId, Date startDate, Integer days) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			Doctor doctor = doctorRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
			BranchMaster branch = branchRepo.findById(branchId)
					.orElseThrow(() -> new RuntimeException("Branch not found"));

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);

			List<DoctorRoster> rosterList = new ArrayList<>();

			for (int i = 0; i < days; i++) {
				DoctorRoster roster = new DoctorRoster();
				roster.setDoctor(doctor);
				roster.setBranchMaster(branch);
				roster.setDate(calendar.getTime());
				roster.setStartTime(Time.valueOf("09:00:00"));
				roster.setEndTime(Time.valueOf("17:00:00"));
				roster.setStatus(true);

				try {
					DoctorRoster savedRoster = webDoctorRosterRepo.save(roster);
					rosterList.add(savedRoster);
				} catch (Exception e) {
					log.error("Failed to save roster for date " + calendar.getTime() + " : " + e.getMessage());
					throw new RuntimeException("Error saving roster for date " + calendar.getTime(), e);
				}

				calendar.add(Calendar.DAY_OF_YEAR, 1);
			}

			responseDTO.setStatusCode(201);
			responseDTO.setMessage("Doctor Rosters created successfully");
			responseDTO.setData(rosterList);
			responseDTO.setStatus(true);

			log.info("Doctor Rosters created successfully for " + days + " days.");
		} catch (Exception e) {
			log.error("Exception occurred while generating doctor roster: " + e.getMessage(), e);
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("Exception occurred: " + e.getMessage());
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO getAvailableDoctors(Date date, Integer branchId, Integer specialityId) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			List<DoctorRoster> doctorRoster = webDoctorRosterRepo.findAvailableDoctors(date, branchId, specialityId);

			if (doctorRoster.isEmpty()) {
				responseDTO.setStatus(false);
				responseDTO.setMessage("No doctors found for the selected date, branch, and speciality");
				responseDTO.setData(null);
				responseDTO.setStatusCode(404);
			} else {
				responseDTO.setStatus(true);
				responseDTO.setMessage("Doctors fetched successfully");
				responseDTO.setData(doctorRoster);
				responseDTO.setStatusCode(200);
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus(false);
			responseDTO.setMessage("An error occurred while fetching doctors");
			responseDTO.setData(new ArrayList<>());
			responseDTO.setStatusCode(500);
		}

		return responseDTO;
	}

	@Override
	public ResponseDTO getDoctorsBySpecialist(Integer specialityId) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			List<DoctorSpecialityMapping> doctorSpecialityMappings = docSpecialityMapRepo
					.findBySpecialitySpecialityId(specialityId);
			List<Doctor> doctors = new ArrayList<>();

			for (DoctorSpecialityMapping mapping : doctorSpecialityMappings) {
				doctors.add(mapping.getDoctor());
			}

			if (doctors.isEmpty()) {
				responseDTO.setMessage("No doctors found for the given speciality");
				responseDTO.setStatus(false);
				responseDTO.setStatusCode(404);
			} else {
				responseDTO.setMessage("Doctors fetched successfully");
				responseDTO.setStatus(true);
				responseDTO.setStatusCode(200);
				responseDTO.setData(doctors);
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setMessage("Error occurred while fetching doctors");
			responseDTO.setStatus(false);
			responseDTO.setStatusCode(500);
		}

		return responseDTO;
	}

	
}
