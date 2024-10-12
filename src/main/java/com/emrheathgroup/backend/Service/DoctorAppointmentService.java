package com.emrheathgroup.backend.Service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.AdminDTOs.DoctorAppointmentDto;
import com.emrheathgroup.backend.DTOs.AppointmentDTOs.AppointmentRescheduleDto;
import com.emrheathgroup.backend.DTOs.AppointmentDTOs.BookAppointmentRequestDto;
import com.emrheathgroup.backend.DTOs.AppointmentDTOs.CancelAppointmentDto;
import com.emrheathgroup.backend.Entity.DoctorAppointment;

@Component
public interface DoctorAppointmentService {

	ResponseDTO MobBookAppointmentByReceptionalist(DoctorAppointmentDto appointmentDto);

	ResponseDTO webBookAppointment(BookAppointmentRequestDto requestDto);

	ResponseDTO getAppointmentsByDoctorID(Integer doctorId);

	ResponseDTO rescheduleAppointment(AppointmentRescheduleDto rescheduleDto);

	ResponseDTO cancelAppointment(CancelAppointmentDto cancelAppointmentDto);

	Page<DoctorAppointment> getAppointmentsWithFilters(String appointmentDate, String mrdNo, String nationalId,
			String mobile, String patientName, String doctorName, String status, int page, int size);

	ResponseDTO getAppointsByDocWithFilters(Integer doctorId, String filters);

	ResponseDTO getPatientsByDocIdWithFilters(Integer doctorId, String filters);


}
