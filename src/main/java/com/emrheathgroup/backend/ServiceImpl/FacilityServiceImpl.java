package com.emrheathgroup.backend.ServiceImpl;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.AppointmentDTOs.BookFacilityRequestDto;
import com.emrheathgroup.backend.DTOs.DoctorDTOs.BlockAvailabilityRequestDto;
import com.emrheathgroup.backend.Entity.BranchMaster;
import com.emrheathgroup.backend.Entity.Doctor;
import com.emrheathgroup.backend.Entity.FacilityBlock;
import com.emrheathgroup.backend.Entity.FacilityRoster;
import com.emrheathgroup.backend.Entity.FacilityScheduling;
import com.emrheathgroup.backend.Entity.Patient;
import com.emrheathgroup.backend.Enumeration.Status;
import com.emrheathgroup.backend.Repository.BranchRepo;
import com.emrheathgroup.backend.Repository.DoctorRepo;
import com.emrheathgroup.backend.Repository.FacilityBlockRepo;
import com.emrheathgroup.backend.Repository.FacilityRosterRepo;
import com.emrheathgroup.backend.Repository.FacilitySchedulingRepo;
import com.emrheathgroup.backend.Repository.PatientRepo;
import com.emrheathgroup.backend.Service.FacilityService;

import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class FacilityServiceImpl implements FacilityService {

	@Autowired
	private FacilitySchedulingRepo facilitySchedulingRepo;

	@Autowired
	private FacilityRosterRepo facilityRosterRepo;

	@Autowired
	private PatientRepo patientRepo;

	@Autowired
	private DoctorRepo doctorRepo;

	@Autowired
	private BranchRepo branchRepo;

	@Autowired
	private FacilityBlockRepo facilityBlockRepo;

	private static final Logger log = LoggerFactory.getLogger(FacilityServiceImpl.class);

	@Override
	public ResponseDTO bookFacility(BookFacilityRequestDto bookFacilityRequestDto) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {

			Integer rosterId = bookFacilityRequestDto.getRosterId();
			Integer doctorId = bookFacilityRequestDto.getDoctorId();
			Date scheduleDate = bookFacilityRequestDto.getScheduleDate();
			Time scheduleTime = bookFacilityRequestDto.getScheduleTime();
			Time endTime = bookFacilityRequestDto.getEndTime();
			String mrdNo = bookFacilityRequestDto.getMrdNo();

			Optional<FacilityRoster> facilityOpt = facilityRosterRepo.findById(rosterId);
			if (!facilityOpt.isPresent()) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("Invalid facility ID.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			FacilityRoster facRoster = facilityOpt.get();

			Optional<Doctor> doctorOpt = doctorRepo.findById(doctorId);
			if (!doctorOpt.isPresent()) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("Invalid doctor ID.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			Doctor doctor = doctorOpt.get();

			Optional<FacilityRoster> rosterOpt = facilityRosterRepo.findByRosterIdAndDate(rosterId, scheduleDate);
			if (!rosterOpt.isPresent()) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("No roster found for the facility on the given date.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			boolean isBlocked = facilityBlockRepo.isTimeBlocked(rosterId, scheduleDate, scheduleTime, endTime);
			if (isBlocked) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("The selected time slot is blocked.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			boolean timeAlreadyBooked = facilitySchedulingRepo.existsByRosterIdAndScheduleDateAndScheduleTime(facRoster,
					scheduleDate, scheduleTime);
			if (timeAlreadyBooked) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("The selected time slot is already booked.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			if (scheduleTime.before(facRoster.getStartTime()) || endTime.after(facRoster.getEndTime())) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("The selected time is outside the facility's available time.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			Patient patient;
			if (mrdNo != null && !mrdNo.isEmpty()) {
				Optional<Patient> patientOpt = patientRepo.findByMrdNo(mrdNo);
				if (patientOpt.isPresent()) {
					patient = patientOpt.get();
				} else {
					patient = createNewPatient(bookFacilityRequestDto);
				}
			} else {
				patient = createNewPatient(bookFacilityRequestDto);
			}

			FacilityScheduling scheduling = new FacilityScheduling();
			scheduling.setRosterId(facRoster);
			scheduling.setDoctorId(doctor);
			scheduling.setScheduleDate(scheduleDate);
			scheduling.setScheduleTime(scheduleTime);
			scheduling.setEndTime(endTime);
			scheduling.setScheduleType(bookFacilityRequestDto.getScheduleType());
			scheduling.setScheduleStatus(true);
			scheduling.setCreatedBy("WebReceptionalist");
			scheduling.setStatus(Status.Active);
			facilitySchedulingRepo.save(scheduling);

			responseDTO.setData(scheduling);
			responseDTO.setStatusCode(200);
			responseDTO.setMessage("Facility Booked Successfully");
			responseDTO.setStatus(true);

		} catch (Exception e) {
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("An error occurred: " + e.getMessage());
			responseDTO.setStatus(false);
			e.printStackTrace();
		}

		return responseDTO;
	}

	private Patient createNewPatient(BookFacilityRequestDto bookFacilityRequestDto) {
		Patient patient = new Patient();
		patient.setMrdNo(generateNewMrdNo());
		patient.setName(bookFacilityRequestDto.getPatientName());
		patient.setDob(bookFacilityRequestDto.getDob());
		patient.setAge(bookFacilityRequestDto.getAge());
		patient.setGender(bookFacilityRequestDto.getGender());
		patient.setPhoneNo(bookFacilityRequestDto.getMobile());
		patient.setEmailId(bookFacilityRequestDto.getEmail());
		patient.setInsuranceName(bookFacilityRequestDto.getInsuranceName());
		patient.setStatus(Status.Active);
		return patientRepo.save(patient);
	}

	private String generateNewMrdNo() {
		String lastMrdNo = patientRepo.findTopByOrderByMrdNoDesc().map(Patient::getMrdNo).orElse("mrdNo000");

		int lastNumber;
		try {
			lastNumber = Integer.parseInt(lastMrdNo.replace("mrdNo", ""));
		} catch (NumberFormatException e) {
			lastNumber = 0;
		}

		int newNumber = lastNumber + 1;
		return String.format("mrdNo%03d", newNumber);
	}

	@Override
	public ResponseDTO getAllFacilitiesByBranchAndDate(Integer branchId, Date date) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			BranchMaster branch = branchRepo.findById(branchId)
					.orElseThrow(() -> new RuntimeException("Branch not found"));

			List<FacilityRoster> distinctFacilities = facilityRosterRepo.findDistinctFacilitiesByBranchAndDate(branchId,
					date);

			responseDTO.setData(distinctFacilities);
			responseDTO.setStatusCode(200);
			responseDTO.setMessage("Facilities Fetched Successfully.");
			responseDTO.setStatus(true);
		} catch (RuntimeException e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("An error occurred: " + e.getMessage());
			responseDTO.setStatus(false);
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO getFacilitySchedulesWithFilters(String facilityName, String scheduleDate, String status,
			String patientName, String mrdNo, String email, String mobile, int page, int size) {

		ResponseDTO responseDTO = new ResponseDTO();
		Pageable pageable = PageRequest.of(page, size);

		Page<FacilityScheduling> schedules = facilitySchedulingRepo.findAll((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (facilityName != null && !facilityName.isEmpty()) {
				predicates.add(cb.like(root.get("rosterId").get("facilityName"), "%" + facilityName + "%"));
			}

			if (scheduleDate != null && !scheduleDate.isEmpty()) {
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					predicates.add(cb.equal(root.get("scheduleDate"), dateFormat.parse(scheduleDate)));
				} catch (ParseException e) {
					responseDTO.setStatusCode(400);
					responseDTO.setMessage("Invalid schedule date format.");
					responseDTO.setStatus(false);
					return null;
				}
			}

			if (status != null && !status.isEmpty()) {
				predicates.add(cb.equal(root.get("status"), status));
			}

			if (patientName != null && !patientName.isEmpty()) {
				predicates.add(cb.like(root.get("patientId").get("name"), "%" + patientName + "%"));
			}

			if (mrdNo != null && !mrdNo.isEmpty()) {
				predicates.add(cb.equal(root.get("patientId").get("mrdNo"), mrdNo));
			}

			if (email != null && !email.isEmpty()) {
				predicates.add(cb.equal(root.get("patientId").get("email"), email));
			}

			if (mobile != null && !mobile.isEmpty()) {
				predicates.add(cb.equal(root.get("patientId").get("mobile"), mobile));
			}

			query.orderBy(cb.asc(root.get("scheduleDate")));
			return cb.and(predicates.toArray(new Predicate[0]));
		}, pageable);

		if (schedules == null || schedules.isEmpty()) {
			responseDTO.setStatusCode(404);
			responseDTO.setMessage("No facility schedules found with the provided filters.");
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
		} else {
			responseDTO.setStatusCode(200);
			responseDTO.setMessage("Facility schedules fetched successfully.");
			responseDTO.setStatus(true);
			responseDTO.setData(schedules);
		}

		return responseDTO;
	}

	@Override
	public ResponseDTO getFacilitySchedulesById(Integer id) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			Optional<FacilityScheduling> schedulingOpt = facilitySchedulingRepo.findById(id);
			if (!schedulingOpt.isPresent()) {
				responseDTO.setStatusCode(404);
				responseDTO.setMessage("Schedule not found.");
				responseDTO.setStatus(false);
				responseDTO.setData(new ArrayList<>());
				return responseDTO;
			}

			FacilityScheduling scheduling = schedulingOpt.get();
			responseDTO.setData(Collections.singletonList(scheduling));
			responseDTO.setStatusCode(200);
			responseDTO.setMessage("Facility schedule fetched successfully By Id.");
			responseDTO.setStatus(true);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("An error occurred: " + e.getMessage());
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO blockFacilityAvailability(BlockAvailabilityRequestDto blockAvailabilityRequestDto) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			Integer rosterId = blockAvailabilityRequestDto.getRosterId();
			Integer branchId = blockAvailabilityRequestDto.getBranchId();
			Date blockDate = blockAvailabilityRequestDto.getDate();
			Time blockStartTime = blockAvailabilityRequestDto.getStartTime();
			Time blockEndTime = blockAvailabilityRequestDto.getEndTime();
			String title = blockAvailabilityRequestDto.getTitle();

			Optional<BranchMaster> branchOpt = branchRepo.findById(branchId);
			if (!branchOpt.isPresent()) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("Branch Id Not Found.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			Optional<FacilityRoster> facilityOpt = facilityRosterRepo.findById(rosterId);
			if (!facilityOpt.isPresent()) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("Facility Id Not Found.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			FacilityRoster facility = facilityOpt.get();

			Optional<FacilityRoster> rosterOpt = facilityRosterRepo.findByRosterIdAndDate(rosterId, blockDate);
			if (!rosterOpt.isPresent()) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("No roster found for the facility on the given date.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			FacilityRoster roster = rosterOpt.get();

			log.debug("Roster Start Time: {}", roster.getStartTime());
			log.debug("Roster End Time: {}", roster.getEndTime());
			log.debug("Block Start Time: {}", blockStartTime);
			log.debug("Block End Time: {}", blockEndTime);

			if (blockStartTime.before(roster.getStartTime()) || blockEndTime.after(roster.getEndTime())) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("Block time is outside the facility's available time as per the roster.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			FacilityBlock blockedFacility = new FacilityBlock();
			blockedFacility.setRosterId(facility);
			blockedFacility.setBlockDate(blockDate);
			blockedFacility.setBlockStartTime(blockStartTime);
			blockedFacility.setBlockEndTime(blockEndTime);
			blockedFacility.setTitle(title);
			blockedFacility.setStatus(Status.Active);
			facilityBlockRepo.save(blockedFacility);

			responseDTO.setStatusCode(200);
			responseDTO.setData(blockedFacility);
			responseDTO.setMessage("Facility availability blocked successfully.");
			responseDTO.setStatus(true);

		} catch (Exception e) {
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("An error occurred: " + e.getMessage());
			responseDTO.setStatus(false);
			e.printStackTrace();
		}

		return responseDTO;
	}

	@Override
	public ResponseDTO generateFacilityRoster(Integer branchId, String facilityName, Date startDate, Integer days) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			BranchMaster branch = branchRepo.findById(branchId)
					.orElseThrow(() -> new RuntimeException("Branch not found"));

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);

			List<FacilityRoster> rosterList = new ArrayList<>();

			for (int i = 0; i < days; i++) {
				FacilityRoster roster = new FacilityRoster();
				roster.setBranchId(branch);
				roster.setFacilityName(facilityName);
				roster.setDate(calendar.getTime());
				roster.setStartTime(Time.valueOf("09:00:00"));
				roster.setEndTime(Time.valueOf("17:00:00"));
				roster.setStatus(Status.Active);

				FacilityRoster savedRoster = facilityRosterRepo.save(roster);
				rosterList.add(savedRoster);

				calendar.add(Calendar.DAY_OF_YEAR, 1);
			}

			responseDTO.setStatus(true);
			responseDTO.setMessage("Facility Rosters created successfully");
			responseDTO.setStatusCode(201);
			responseDTO.setData(rosterList);
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.setStatus(false);
			responseDTO.setMessage("Error occurred while creating facility roster: " + e.getMessage());
			responseDTO.setStatusCode(500);
		}
		return responseDTO;
	}
}
