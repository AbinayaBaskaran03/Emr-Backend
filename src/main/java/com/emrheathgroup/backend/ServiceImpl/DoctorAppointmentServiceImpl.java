package com.emrheathgroup.backend.ServiceImpl;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.AdminDTOs.DoctorAppointmentDto;
import com.emrheathgroup.backend.DTOs.AdminDTOs.MobPatientTimeSlotDto;
import com.emrheathgroup.backend.DTOs.AdminDTOs.TimeSlotDto;
import com.emrheathgroup.backend.DTOs.AppointmentDTOs.AppointmentRescheduleDto;
import com.emrheathgroup.backend.DTOs.AppointmentDTOs.BookAppointmentRequestDto;
import com.emrheathgroup.backend.DTOs.AppointmentDTOs.CancelAppointmentDto;
import com.emrheathgroup.backend.DTOs.AppointmentDTOs.GetAppointmentDto;
import com.emrheathgroup.backend.DTOs.PatientDTOs.GetPatientAppointmentDto;
import com.emrheathgroup.backend.Entity.BranchMaster;
import com.emrheathgroup.backend.Entity.Doctor;
import com.emrheathgroup.backend.Entity.DoctorAppointment;
import com.emrheathgroup.backend.Entity.DoctorRoster;
import com.emrheathgroup.backend.Entity.DoctorSpecialityMapping;
import com.emrheathgroup.backend.Entity.Patient;
import com.emrheathgroup.backend.Entity.Speciality;
import com.emrheathgroup.backend.Enumeration.ExistsResponse;
import com.emrheathgroup.backend.Enumeration.Status;
import com.emrheathgroup.backend.Repository.BranchRepo;
import com.emrheathgroup.backend.Repository.DocSpecialityMapRepo;
import com.emrheathgroup.backend.Repository.DoctorAppointmentRepo;
import com.emrheathgroup.backend.Repository.DoctorRepo;
import com.emrheathgroup.backend.Repository.DoctorRosterRepo;
import com.emrheathgroup.backend.Repository.PatientRepo;
import com.emrheathgroup.backend.Repository.SpecialityRepo;
import com.emrheathgroup.backend.Service.DoctorAppointmentService;

import jakarta.persistence.criteria.Predicate;

@Service
public class DoctorAppointmentServiceImpl implements DoctorAppointmentService {

	private static final Logger log = LoggerFactory.getLogger(DoctorAppointmentServiceImpl.class);

	@Autowired
	private BranchRepo branchRepo;

	@Autowired
	private DoctorRepo doctorRepo;

	@Autowired
	private PatientRepo patientRepo;

	@Autowired
	private DoctorAppointmentRepo doctorAppointmentRepo;

	@Autowired(required = true)
	private DoctorRosterRepo doctorRosterRepo;

	@Autowired
	private DocSpecialityMapRepo docSpecialityMapRepo;

	@Autowired
	private SpecialityRepo specialityRepo;

	@Override
	public ResponseDTO MobBookAppointmentByReceptionalist(DoctorAppointmentDto appointmentDto) {
		ResponseDTO responseDTO = new ResponseDTO();
		List<String> errors = new ArrayList<>();

		try {
			Speciality speciality = specialityRepo.findById(appointmentDto.getSpecialityId())
					.orElseThrow(() -> new RuntimeException("Speciality not found"));

			List<DoctorSpecialityMapping> doctorSpecialities = docSpecialityMapRepo
					.findBySpecialityAndDoctor_BranchMaster_BranchId(speciality, appointmentDto.getBranchId());

			if (doctorSpecialities.isEmpty()) {
				responseDTO.setStatus(false);
				responseDTO.setMessage("No doctors available for the given specialization ID and branch");
				responseDTO.setStatusCode(400);
				return responseDTO;
			}

			List<Doctor> doctors = doctorSpecialities.stream().map(DoctorSpecialityMapping::getDoctor)
					.collect(Collectors.toList());

			Doctor selectedDoctor = doctors.stream()
					.filter(doctor -> doctor.getDoctorId().equals(appointmentDto.getDocId())).findFirst()
					.orElseThrow(() -> new RuntimeException("Doctor not found"));

			Optional<DoctorRoster> rosterOpt = doctorRosterRepo.findByDoctorAndDate(selectedDoctor,
					appointmentDto.getAppointmentDate());

			if (rosterOpt.isEmpty()) {
				responseDTO.setStatus(false);
				responseDTO.setMessage("Doctor is not available on the given date");
				responseDTO.setStatusCode(400);
				return responseDTO;
			}

			DoctorRoster doctorRoster = rosterOpt.get();

			List<TimeSlotDto> availableSlots = generateTimeSlots(doctorRoster.getStartTime(),
					doctorRoster.getEndTime());

			List<DoctorAppointment> existingAppointments = doctorAppointmentRepo
					.findByDoctor_DoctorIdAndAppointmentDate(selectedDoctor.getDoctorId(),
							appointmentDto.getAppointmentDate());

			Set<Time> bookedSlots = existingAppointments.stream().map(DoctorAppointment::getAppointmentTime)
					.collect(Collectors.toSet());

			List<Map<String, String>> bookedSlotRanges = formatTimeSlotsAsRanges(new ArrayList<>(bookedSlots));
			availableSlots.removeIf(slot -> bookedSlots.contains(slot.getSlotTiming()));
			List<Map<String, String>> availableSlotRanges = formatTimeSlotsAsRanges(
					availableSlots.stream().map(TimeSlotDto::getSlotTiming).collect(Collectors.toList()));

			Map<String, Object> appointmentDetails = new HashMap<>();
			appointmentDetails.put("appointmentDate", appointmentDto.getAppointmentDate());
			appointmentDetails.put("branchId", appointmentDto.getBranchId());
			appointmentDetails.put("branchName", "Main Branch");
			appointmentDetails.put("specialityId", appointmentDto.getSpecialityId());
			// appointmentDetails.put("specialityName", selectedDoctor.getSpeciality());
			appointmentDetails.put("doctorId", selectedDoctor.getDoctorId());
			appointmentDetails.put("doctorName", selectedDoctor.getName());
			appointmentDetails.put("doctorExperience", selectedDoctor.getExperience() + " Years");

			Map<String, Object> doctorAvailability = new HashMap<>();
			doctorAvailability.put("startTime", doctorRoster.getStartTime().toString());
			doctorAvailability.put("endTime", doctorRoster.getEndTime().toString());
			doctorAvailability.put("bookedSlos", bookedSlotRanges);
			doctorAvailability.put("availableSlots", availableSlotRanges);
			appointmentDetails.put("doctorAvailability", doctorAvailability);

			List<Map<String, Object>> patients = new ArrayList<>();
			for (MobPatientTimeSlotDto mobPatientTimeSlotDto : appointmentDto.getMobPatientTimeSlotDto()) {
				Patient patient = findExistingPatient(mobPatientTimeSlotDto);
				;

				if (patient == null) {
					patient = createNewPatient(mobPatientTimeSlotDto);
					mobPatientTimeSlotDto.setExistsResponse(ExistsResponse.New);
				} else {
					mobPatientTimeSlotDto.setPatientName(patient.getName());
					mobPatientTimeSlotDto.setGender(patient.getGender());
					mobPatientTimeSlotDto.setPhoneNo(patient.getPhoneNo());
					mobPatientTimeSlotDto.setEmail(patient.getEmailId());
					mobPatientTimeSlotDto.setAge(patient.getAge());
					mobPatientTimeSlotDto.setExistsResponse(ExistsResponse.Old);
				}

				if (!isWithinRosterTime(doctorRoster, mobPatientTimeSlotDto.getSlotTiming())) {
					errors.add("The time slot " + mobPatientTimeSlotDto.getSlotTiming()
							+ " is outside the doctor's roster hours.");
					responseDTO.setStatus(false);
					responseDTO.setMessage("The time slot " + mobPatientTimeSlotDto.getSlotTiming()
							+ " is outside the doctor's roster hours.");
					responseDTO.setStatusCode(409);
					return responseDTO;
				}

				if (isTimeSlotAlreadyBooked(selectedDoctor, speciality, appointmentDto.getAppointmentDate(),
						mobPatientTimeSlotDto.getSlotTiming())) {
					errors.add("The time slot " + mobPatientTimeSlotDto.getSlotTiming() + " is already booked.");
					responseDTO.setStatus(false);
					responseDTO.setMessage(
							"The time slot " + mobPatientTimeSlotDto.getSlotTiming() + " is already booked.");
					responseDTO.setStatusCode(409);
					return responseDTO;
				}

				createAppointment(selectedDoctor, doctorRoster.getBranchMaster(), patient,
						appointmentDto.getAppointmentDate(), mobPatientTimeSlotDto.getSlotTiming(), appointmentDto,
						speciality);

				Map<String, Object> patientDetails = new HashMap<>();
				patientDetails.put("existsResponse", mobPatientTimeSlotDto.getExistsResponse().toString());
				patientDetails.put("patientName", mobPatientTimeSlotDto.getPatientName());
				patientDetails.put("gender", mobPatientTimeSlotDto.getGender());
				patientDetails.put("phoneNo", mobPatientTimeSlotDto.getPhoneNo());
				patientDetails.put("email", mobPatientTimeSlotDto.getEmail());
				patientDetails.put("age", mobPatientTimeSlotDto.getAge());

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
				LocalTime fromTime = LocalTime.parse(mobPatientTimeSlotDto.getSlotTiming().toString().substring(0, 5),
						formatter);
				LocalTime toTime = fromTime.plusMinutes(15);

				Map<String, String> slotTiming = new HashMap<>();
				slotTiming.put("from", fromTime.format(formatter));
				slotTiming.put("to", toTime.format(formatter));
				patientDetails.put("slotTiming", slotTiming);

				patients.add(patientDetails);
			}

			appointmentDetails.put("patients", patients);
			responseDTO.setData(appointmentDetails);

			if (!errors.isEmpty()) {
				responseDTO.setStatus(false);
				responseDTO.setMessage(String.join(", ", errors));
			} else {
				responseDTO.setStatus(true);
				responseDTO.setMessage("Appointment booked successfully.");
			}

			responseDTO.setStatusCode(200);

		} catch (Exception e) {
			responseDTO.setStatus(false);
			responseDTO.setMessage("Error: " + e.getMessage());
			responseDTO.setStatusCode(500);
		}

		return responseDTO;
	}

	private List<Map<String, String>> formatTimeSlotsAsRanges(List<Time> timeSlots) {
		List<Map<String, String>> formattedSlots = new ArrayList<>();

		for (Time time : timeSlots) {
			Map<String, String> slotRange = new HashMap<>();
			String timeString = time.toString().substring(0, 5);

			Calendar cal = Calendar.getInstance();
			cal.setTime(time);
			cal.add(Calendar.MINUTE, 15);
			String endTimeString = new Time(cal.getTime().getTime()).toString().substring(0, 5);

			slotRange.put("from", timeString);
			slotRange.put("to", endTimeString);
			formattedSlots.add(slotRange);
		}

		return formattedSlots;
	}

	private List<TimeSlotDto> generateTimeSlots(Time startTime, Time endTime) {
		List<TimeSlotDto> timeSlots = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);

		while (!calendar.getTime().after(endTime)) {
			Time slotStart = new Time(calendar.getTime().getTime());
			TimeSlotDto timeSlot = new TimeSlotDto();
			timeSlot.setSlotTiming(slotStart);
			timeSlots.add(timeSlot);

			calendar.add(Calendar.MINUTE, 15);
		}
		return timeSlots;
	}

	private boolean isTimeSlotAlreadyBooked(Doctor doctor, Speciality speciality, Date appointmentDate,
			Time slotTiming) {
		List<DoctorAppointment> existingAppointments = doctorAppointmentRepo
				.findByDoctor_DoctorIdAndSpeciality_SpecialityIdAndAppointmentDate(doctor.getDoctorId(),
						speciality.getSpecialityId(), appointmentDate);

		return existingAppointments.stream()
				.anyMatch(appointment -> appointment.getAppointmentTime().equals(slotTiming));
	}

	private boolean isWithinRosterTime(DoctorRoster doctorRoster, Time slotTiming) {
		return !slotTiming.before(doctorRoster.getStartTime()) && !slotTiming.after(doctorRoster.getEndTime());
	}

	private void createAppointment(Doctor selectedDoctor, BranchMaster branchId, Patient patient, Date appointmentDate,
			Time slotTiming, DoctorAppointmentDto appointmentDto, Speciality speciality) {
		DoctorAppointment doctorAppointment = new DoctorAppointment();
		doctorAppointment.setDoctor(selectedDoctor);
		doctorAppointment.setBranchMaster(branchId);
		doctorAppointment.setPatient(patient);
		doctorAppointment.setAppointmentDate(appointmentDate);
		doctorAppointment.setAppointmentTime(slotTiming);
		doctorAppointment.setScheduleType(appointmentDto.getScheduleType());
		doctorAppointment.setSpeciality(speciality);
		doctorAppointment.setCreatedBy("Mob_Receptionalist");
		doctorAppointment.setUpdatedBy(doctorAppointment.getCreatedBy());
		doctorAppointment.setActiveStatus(Status.Active);
		doctorAppointmentRepo.save(doctorAppointment);
	}

	private Patient createNewPatient(MobPatientTimeSlotDto mobPatientTimeSlotDto) {
		Patient newPatient = new Patient();
		newPatient.setName(mobPatientTimeSlotDto.getPatientName());
		newPatient.setEmailId(mobPatientTimeSlotDto.getEmail());
		newPatient.setPhoneNo(mobPatientTimeSlotDto.getPhoneNo());
		newPatient.setGender(mobPatientTimeSlotDto.getGender());
		newPatient.setAge(mobPatientTimeSlotDto.getAge());
		newPatient.setMrdNo(generateUniqueMrdNo());
		patientRepo.save(newPatient);
		return newPatient;
	}

	private String generateUniqueMrdNo() {
		int randomNumber = (int) (Math.random() * 100000);
		return "MRD" + randomNumber;
	}

	private Patient findExistingPatient(MobPatientTimeSlotDto mobPatientTimeSlotDto) {
		List<Patient> patients = new ArrayList<>();

		if (mobPatientTimeSlotDto.getMrdNo() != null && !mobPatientTimeSlotDto.getMrdNo().isEmpty()) {
			patients = patientRepo.findAllByMrdNo(mobPatientTimeSlotDto.getMrdNo());
			if (!patients.isEmpty()) {
				return checkSinglePatient(patients);
			}
		}

		if (mobPatientTimeSlotDto.getPhoneNo() != null && !mobPatientTimeSlotDto.getPhoneNo().isEmpty()) {
			patients = patientRepo.findAllByPhoneNo(mobPatientTimeSlotDto.getPhoneNo());
			if (!patients.isEmpty()) {
				return checkSinglePatient(patients);
			}
		}

		if (mobPatientTimeSlotDto.getEmail() != null && !mobPatientTimeSlotDto.getEmail().isEmpty()) {
			patients = patientRepo.findAllByEmailId(mobPatientTimeSlotDto.getEmail());
			if (!patients.isEmpty()) {
				return checkSinglePatient(patients);
			}
		}

		if (mobPatientTimeSlotDto.getPatientName() != null && !mobPatientTimeSlotDto.getPatientName().isEmpty()) {
			patients = patientRepo.findAllByName(mobPatientTimeSlotDto.getPatientName());
			if (!patients.isEmpty()) {
				return checkSinglePatient(patients);
			}
		}

		return null;
	}

	private Patient checkSinglePatient(List<Patient> patients) {
		if (patients.size() > 1) {
			throw new IllegalStateException("Multiple patients found with the provided details.");
		}
		return patients.isEmpty() ? null : patients.get(0);
	}

	@Override
	public ResponseDTO webBookAppointment(BookAppointmentRequestDto requestDto) {
		ResponseDTO responseDTO = new ResponseDTO();

		try {
			Integer branchId = requestDto.getBranchId();
			Date appointmentDate = requestDto.getAppointmentDate();

			Integer doctorId = requestDto.getDoctorId();
			Time appointmentTime = requestDto.getAppointmentTime();
			String mrdNo = requestDto.getMrdNo();

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

			List<DoctorRoster> rosters = doctorRosterRepo.findByDoctor(doctor);
			if (rosters.isEmpty()) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("No available schedule for the doctor.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			boolean doctorAvailable = rosters.stream().anyMatch(roster -> isAvailable(roster, appointmentTime));

			if (!doctorAvailable) {
				log.error("Doctor is not available at the selected time");
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("Doctor is not available at the selected time.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			boolean timeAlreadyBooked = doctorAppointmentRepo
					.existsByDoctor_DoctorIdAndAppointmentDateAndAppointmentTime(doctor, appointmentDate,
							appointmentTime);

			if (timeAlreadyBooked) {
				log.error("The selected time slot is already booked.");
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("The selected time slot is already booked.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			Patient patient;
			if (mrdNo != null && !mrdNo.isEmpty()) {
				log.info("Checking patient with MRD number: {}", mrdNo);
				Optional<Patient> patientOpt = patientRepo.findByMrdNo(mrdNo);
				if (patientOpt.isPresent()) {
					patient = patientOpt.get();
				} else {
					log.error("Patient with MRD number {} not found.", mrdNo);
					responseDTO.setStatusCode(400);
					responseDTO.setMessage("Invalid MRD number.");
					responseDTO.setStatus(false);
					return responseDTO;
				}
			} else {
				if (patientRepo.existsByPhoneNo(requestDto.getPhoneNo())) {
					responseDTO.setStatusCode(400);
					responseDTO.setMessage("Mobile number already exists.");
					responseDTO.setStatus(false);
					return responseDTO;
				}

				if (patientRepo.existsByEmailId(requestDto.getEmail())) {
					responseDTO.setStatusCode(400);
					responseDTO.setMessage("Email already exists.");
					responseDTO.setStatus(false);
					return responseDTO;
				}

				patient = createNewPatient(requestDto, doctor);
			}

			DoctorAppointment appointment = new DoctorAppointment();
			appointment.setPatient(patient);
			appointment.setDoctor(doctor);
			appointment.setBranchMaster(branch);
			appointment.setAppointmentDate(appointmentDate);
			appointment.setAppointmentTime(appointmentTime);
			appointment.setScheduleType(requestDto.getScheduleType());
			appointment.setNotes(requestDto.getNotes());
			appointment.setCreatedBy("Web_Receptionalist");
			appointment.setAppointmentStatus(requestDto.getAppointmentStatus());
			appointment.setActiveStatus(Status.Active);
			doctorAppointmentRepo.save(appointment);

			responseDTO.setData(appointment);
			responseDTO.setStatusCode(200);
			responseDTO.setMessage("Appointment Booked Successfully");
			responseDTO.setStatus(true);
			log.info("Doctor Appointment Booked Successfully");

		} catch (Exception e) {
			log.error("Error booking appointment", e);
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("An error occurred: " + e.getMessage());
			responseDTO.setStatus(false);
		}

		return responseDTO;
	}

	private boolean isAvailable(DoctorRoster roster, Time appointmentTime) {
		return !appointmentTime.before(roster.getStartTime()) && !appointmentTime.after(roster.getEndTime());
	}

	private Patient createNewPatient(BookAppointmentRequestDto requestDto, Doctor doctor) {
		Patient patient = new Patient();
		patient.setMrdNo(generateNewMrdNo());
		patient.setName(requestDto.getPatientName());
		patient.setDob(requestDto.getDob());
		patient.setAge(requestDto.getAge());
		patient.setGender(requestDto.getGender());
		patient.setPhoneNo(requestDto.getPhoneNo());
		patient.setEmailId(requestDto.getEmail());
		patient.setInsuranceName(requestDto.getInsuranceName());
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
	public Page<DoctorAppointment> getAppointmentsWithFilters(String appointmentDate, String mrdNo, String nationalId,
			String mobile, String patientName, String doctorName, String status, int page, int size) {

		Pageable pageable = PageRequest.of(page, size);

		return doctorAppointmentRepo.findAll((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (appointmentDate != null && !appointmentDate.isEmpty()) {
				try {
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(appointmentDate);
					predicates.add(cb.equal(root.get("appointmentDate"), date));
				} catch (ParseException e) {
				}
			}

			if (mrdNo != null && !mrdNo.isEmpty()) {
				predicates.add(cb.equal(root.get("patient").get("mrdNo"), mrdNo));
			}
			if (nationalId != null && !nationalId.isEmpty()) {
				predicates.add(cb.equal(root.get("patient").get("nationalId"), nationalId));
			}
			if (mobile != null && !mobile.isEmpty()) {
				predicates.add(cb.equal(root.get("patient").get("mobile"), mobile));
			}
			if (patientName != null && !patientName.isEmpty()) {
				predicates.add(cb.like(root.get("patient").get("name"), "%" + patientName + "%"));
			}
			if (doctorName != null && !doctorName.isEmpty()) {
				predicates.add(cb.like(root.get("doctor").get("name"), "%" + doctorName + "%"));
			}
			if (status != null && !status.isEmpty()) {
				predicates.add(cb.equal(root.get("appointmentStatus"), status));
			}

			query.orderBy(cb.asc(root.get("appointmentDate")));
			return cb.and(predicates.toArray(new Predicate[0]));
		}, pageable);
	}

	@Override
	public ResponseDTO getAppointmentsByDoctorID(Integer doctorId) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			List<DoctorAppointment> appointments = doctorAppointmentRepo.findByDoctor_DoctorId(doctorId);

			if (appointments.isEmpty()) {
				responseDTO.setStatusCode(404);
				responseDTO.setMessage("No appointments found for the doctor.");
				responseDTO.setStatus(false);
				responseDTO.setData(new ArrayList<>());
			} else {
				responseDTO.setStatusCode(200);
				responseDTO.setMessage("Appointments fetched successfully.");
				responseDTO.setStatus(true);
				responseDTO.setData(appointments);
			}
		} catch (Exception e) {
			log.error("Error retrieving appointments by doctor ID", e);
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("An error occurred: " + e.getMessage());
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO rescheduleAppointment(AppointmentRescheduleDto requestDto) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			Integer appointmentId = requestDto.getAppointmentId();
			Integer doctorId = requestDto.getDoctorId();
			Date newAppointmentDate = requestDto.getAppointmentDate();
			Time newAppointmentTime = requestDto.getAppointmentTime();

			Optional<DoctorAppointment> appointmentOpt = doctorAppointmentRepo.findById(appointmentId);
			if (!appointmentOpt.isPresent()) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("Invalid appointment ID.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			DoctorAppointment appointment = appointmentOpt.get();

			Optional<Doctor> doctorOpt = doctorRepo.findById(doctorId);
			if (!doctorOpt.isPresent()) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("Invalid doctor ID.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			Doctor doctor = doctorOpt.get();
			List<DoctorRoster> rosters = doctorRosterRepo.findByDoctor(doctor);
			if (rosters.isEmpty() || rosters.stream().noneMatch(roster -> isAvailable(roster, newAppointmentTime))) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("Doctor is not available at the selected time.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			boolean timeAlreadyBooked = doctorAppointmentRepo
					.existsByDoctor_DoctorIdAndAppointmentDateAndAppointmentTime(doctor, newAppointmentDate,
							newAppointmentTime);

			if (timeAlreadyBooked) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("The selected time slot is already booked.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			appointment.setDoctor(doctor);
			appointment.setAppointmentDate(newAppointmentDate);
			appointment.setAppointmentTime(newAppointmentTime);
			appointment.setUpdatedBy(appointment.getCreatedBy());
			doctorAppointmentRepo.save(appointment);

			responseDTO.setData(appointment);
			responseDTO.setStatusCode(200);
			responseDTO.setMessage("Appointment rescheduled successfully");
			responseDTO.setStatus(true);
		} catch (Exception e) {
			log.error("Error rescheduling appointment", e);
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("An error occurred: " + e.getMessage());
			responseDTO.setStatus(false);
		}

		return responseDTO;
	}

	@Override
	public ResponseDTO cancelAppointment(CancelAppointmentDto cancelAppointmentDto) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			Optional<DoctorAppointment> appointmentOpt = doctorAppointmentRepo
					.findById(cancelAppointmentDto.getAppointmentId());
			if (!appointmentOpt.isPresent()) {
				responseDTO.setStatusCode(400);
				responseDTO.setMessage("Invalid appointment ID.");
				responseDTO.setStatus(false);
				return responseDTO;
			}

			DoctorAppointment appointment = appointmentOpt.get();
			appointment.setActiveStatus(Status.Inactive);
			appointment.setAppointmentStatus("Cancelled");
			appointment.setCancellationReason(cancelAppointmentDto.getCancellationReason());
			appointment.setCancelledBy(cancelAppointmentDto.getCancelledBy());

			doctorAppointmentRepo.save(appointment);

			responseDTO.setStatusCode(200);
			responseDTO.setData(cancelAppointmentDto);
			responseDTO.setMessage("Appointment Canceled Successfully");
			responseDTO.setStatus(true);
		} catch (Exception e) {
			log.error("Error canceling appointment", e);
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("An error occurred: " + e.getMessage());
			responseDTO.setStatus(false);
		}

		return responseDTO;
	}

	@Override
	public ResponseDTO getAppointsByDocWithFilters(Integer doctorId, String filters) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			List<DoctorAppointment> appointments = doctorAppointmentRepo.findByDoctor_DoctorId(doctorId);
			List<GetAppointmentDto> appointmentDTOs = new ArrayList<>();

			LocalDate today = LocalDate.now();

			for (DoctorAppointment appointment : appointments) {
				LocalDate appointmentDate = appointment.getAppointmentDate().toInstant().atZone(ZoneId.systemDefault())
						.toLocalDate();
				GetAppointmentDto dto = new GetAppointmentDto();
				dto.setAppointmentId(appointment.getAppointmentId());
				dto.setAppointmentDate(appointment.getAppointmentDate());
				dto.setAppointmentTime(appointment.getAppointmentTime());
				dto.setPatientName(appointment.getPatient().getName());
				dto.setSpeciality(appointment.getSpeciality().getName());
				dto.setAppointmentStatus(appointment.getAppointmentStatus());
				dto.setStatus(appointment.getActiveStatus().toString());

				switch (filters.toLowerCase()) {
				case "today":
					if (appointmentDate.isEqual(today)) {
						appointmentDTOs.add(dto);
					}
					break;
				case "past":
					if (appointmentDate.isBefore(today)) {
						appointmentDTOs.add(dto);
					}
					break;
				case "future":
					if (appointmentDate.isAfter(today)) {
						appointmentDTOs.add(dto);
					}
					break;
				}
			}

			if (appointmentDTOs.isEmpty()) {
				responseDTO.setStatusCode(404);
				responseDTO.setMessage("No appointments found for the doctor");
				responseDTO.setStatus(false);
				responseDTO.setData(new ArrayList<>());
			} else {
				responseDTO.setStatusCode(200);
				responseDTO.setMessage("Appointments fetched successfully");
				responseDTO.setStatus(true);
				responseDTO.setData(appointmentDTOs);
			}
		} catch (Exception e) {
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("An error occurred: " + e.getMessage());
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO getPatientsByDocIdWithFilters(Integer doctorId, String filters) {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			List<DoctorAppointment> appointments = doctorAppointmentRepo.findByDoctor_DoctorId(doctorId);
			List<GetPatientAppointmentDto> patientAppointmentDTOs = new ArrayList<>();

			LocalDate today = LocalDate.now();

			for (DoctorAppointment appointment : appointments) {
				LocalDate appointmentDate = appointment.getAppointmentDate().toInstant().atZone(ZoneId.systemDefault())
						.toLocalDate();
				GetPatientAppointmentDto dto = new GetPatientAppointmentDto();
				dto.setPatientId(appointment.getPatient().getPatientId());
				dto.setPatientName(appointment.getPatient().getName());
				dto.setProblem(appointment.getNotes());
				dto.setAppointmentDate(appointment.getAppointmentDate());
				dto.setAppointmentTime(appointment.getAppointmentTime());
				dto.setLocation(appointment.getBranchMaster().getBranchName());

				switch (filters.toLowerCase()) {
				case "today":
					if (appointmentDate.isEqual(today)) {
						patientAppointmentDTOs.add(dto);
					}
					break;
				case "past":
					if (appointmentDate.isBefore(today)) {
						patientAppointmentDTOs.add(dto);
					}
					break;
				case "future":
					if (appointmentDate.isAfter(today)) {
						patientAppointmentDTOs.add(dto);
					}
					break;
				}
			}

			if (patientAppointmentDTOs.isEmpty()) {
				responseDTO.setStatusCode(404);
				responseDTO.setMessage("No patient appointments found for the doctor");
				responseDTO.setStatus(false);
				responseDTO.setData(new ArrayList<>());
			} else {
				responseDTO.setStatusCode(200);
				responseDTO.setMessage("Patient appointments fetched successfully");
				responseDTO.setStatus(true);
				responseDTO.setData(patientAppointmentDTOs);
			}
		} catch (Exception e) {
			responseDTO.setStatusCode(500);
			responseDTO.setMessage("An error occurred: " + e.getMessage());
			responseDTO.setStatus(false);
			responseDTO.setData(new ArrayList<>());
		}
		return responseDTO;
	}

}
