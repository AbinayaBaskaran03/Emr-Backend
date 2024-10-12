package com.emrheathgroup.backend.Service.Doctor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.emrheathgroup.backend.Enum.SCHEDULE_TYPE;
import com.emrheathgroup.backend.Repository.Doctor.DoctorRepository;

@Service
public class DoctorService {

	private static final String DISPLAY_INDEX = "displayIndex";
	private static final String DISPLAY_NAME = "displayName";
	private static final String TOTAL_COUNT = "totalCount";

	@Autowired
	DoctorRepository doctorRepository;

	public List<Map<String, Object>> getCountForDoctorDashboardData(int doctorId) {
		List<Map<String, Object>> dataList = new ArrayList<>();
		dataList.add(getTodayPatientCount(doctorId));
		dataList.add(getTodaysTotalRevenue(doctorId));
		dataList.add(getPatientApptmntCount(doctorId, SCHEDULE_TYPE.EMERGENCY));
		dataList.add(getPatientApptmntCount(doctorId, SCHEDULE_TYPE.WAITING));
		dataList.add(getPatientApptmntCount(doctorId, SCHEDULE_TYPE.TELE));
		return dataList;
	}

	private Map<String, Object> getTodayPatientCount(int doctorId) {
		Map<String, Object> map = new HashMap();
		map.put(DISPLAY_INDEX, 1);
		map.put(DISPLAY_NAME, "Today's Appointment");
		map.put(TOTAL_COUNT, doctorRepository.getTodayPatientCount(doctorId, new Date()));
		return map;
	}

	private Map<String, Object> getTodaysTotalRevenue(int doctorId) {
		Map<String, Object> map = new HashMap();
		map.put(DISPLAY_INDEX, 2);
		map.put(DISPLAY_NAME, "Revenue");
		map.put(TOTAL_COUNT, doctorRepository.getTodayTotalRevenue(doctorId, new Date()));
		return map;
	}

	private Map<String, Object> getPatientApptmntCount(int doctorId, SCHEDULE_TYPE scheduleType) {
		Map<String, Object> map = new HashMap();
		switch (scheduleType) {
		case EMERGENCY:
			map.put(DISPLAY_INDEX, 3);
			map.put(DISPLAY_NAME, "Emergency Patient");
			break;
		case WAITING:
			map.put(DISPLAY_INDEX, 4);
			map.put(DISPLAY_NAME, "Waiting list");
			break;
		case TELE:
			map.put(DISPLAY_INDEX, 5);
			map.put(DISPLAY_NAME, "Telemedicine Appointment");
			break;
		default:
			break;
		}
		map.put(TOTAL_COUNT, doctorRepository.getPatientApptmntCount(doctorId, new Date(), scheduleType.getValue()));
		return map;
	}

	private String validateRequestStr(String str) {
		str = "".equals(str) ? null : str;
		return str;
	}

	public List<Map<String, Object>> getDataList(int doctorId, int ipOpFlag, String ptName, String status,
			String context, int page, int size) {
		List<Map<String, Object>> dataList = null;
		Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "sNo");
		ptName = validateRequestStr(ptName);
		status = validateRequestStr(status);

		switch (context) {
		case "todaysAppointment":
			dataList = doctorRepository.getTodayPatientData(doctorId, new Date(), ptName, status, pageable);
			dataList = appendLastConsltDate(dataList);
			break;
		case "revenue":
			dataList = doctorRepository.getTodayTotalRevenueData(doctorId, new Date(), pageable);
			break;
		case "emergencyPatient":
			dataList = doctorRepository.getPatientApptmntData(doctorId, new Date(), SCHEDULE_TYPE.EMERGENCY.getValue(),
					ptName, status, pageable);
			dataList = appendLastConsltDate(dataList);
			break;
		case "waitingList":
			dataList = doctorRepository.getPatientApptmntData(doctorId, new Date(), SCHEDULE_TYPE.WAITING.getValue(),
					ptName, status, pageable);
			dataList = appendLastConsltDate(dataList);
			break;
		case "teleAppointment":
			dataList = doctorRepository.getPatientApptmntData(doctorId, new Date(), SCHEDULE_TYPE.TELE.getValue(),
					ptName, status, pageable);
			dataList = appendLastConsltDate(dataList);
			break;
		default:
			break;
		}

		return dataList;
	}

	private List<Map<String, Object>> appendLastConsltDate(List<Map<String, Object>> dataList) {
		List<Map<String, Object>> returnList = new ArrayList<>();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		dataList.stream().collect(Collectors.toList()).forEach(dbMap -> {
			Map<String, Object> map = new HashMap<>();
			Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "appointmentId");
			Integer doctorId = (Integer) dbMap.get("doctorId");
			Integer patientId = (Integer) dbMap.get("patientId");
			Integer specialityId = (Integer) dbMap.get("specialityId");
			String scheduleType = dbMap.containsKey("scheduleType") ? (String) dbMap.get("scheduleType") : null;
			List<Date> result = doctorRepository.getLastConsultaionDate(doctorId, patientId, specialityId, new Date(),
					scheduleType, pageable);

			Date lastConsultationDate = !result.isEmpty() ? result.get(0) : null;
			map.put("lastConsultationDate", lastConsultationDate != null ? df.format(lastConsultationDate) : "");
			map.putAll(dbMap);
			map.remove("doctorId");
			map.remove("patientId");
			map.remove("specialityId");
			map.remove("scheduleType");
			returnList.add(map);
		});
		return returnList;
	}
}
