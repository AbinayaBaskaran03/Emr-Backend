package com.emrheathgroup.backend.DTOs.AppointmentDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelAppointmentDto {

	@NotNull(message = "Appointment ID is required")
	private Integer appointmentId;

	@NotNull(message = "Cancellation reason is required")
	private String cancellationReason;

	@NotNull(message = "Cancelled by is required")
	private String cancelledBy;

}
