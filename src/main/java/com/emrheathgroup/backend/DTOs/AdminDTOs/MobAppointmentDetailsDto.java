package com.emrheathgroup.backend.DTOs.AdminDTOs;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobAppointmentDetailsDto {

	private List<Map<String, String>> availableSlots;
    private List<Map<String, String>> bookedSlots;
}
