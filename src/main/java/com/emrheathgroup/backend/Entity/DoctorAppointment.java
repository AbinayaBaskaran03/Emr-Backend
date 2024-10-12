package com.emrheathgroup.backend.Entity;

import java.sql.Time;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import com.emrheathgroup.backend.Enumeration.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Doctor_Appointment")
public class DoctorAppointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Appointment_Id")
	private Integer appointmentId;

	@ManyToOne
	@JoinColumn(name = "Doctor_Id")
	private Doctor doctor;

	@ManyToOne
	@JoinColumn(name = "Branch_Id")
	private BranchMaster branchMaster;

	@ManyToOne
	@JoinColumn(name = "Patient_Id")
	private Patient patient;

	@Column(name = "Appointment_Date")
	private Date appointmentDate;

	@Column(name = "Appointment_Time")
	private Time appointmentTime;

	@Column(name = "schedule_type")
	private String scheduleType;

	@Lob
	@Column(name = "Notes", columnDefinition = "TEXT")
	private String notes;

	@Column(name = "insurance_name")
	private String insuranceName;

	@Column(name = "revenue")
	private Long revenue;

	@Column(name = "Appointment_Status")
	private String appointmentStatus;

	@Column(name = "Active_Status")
	@Enumerated(EnumType.STRING)
	private Status activeStatus;

	@CreationTimestamp
	@Column(name = "Created_On")
	private Date createdOn;

	@CreatedBy
	@Column(name = "Created_by")
	private String createdBy;

	@UpdateTimestamp
	@Column(name = "Updated_On")
	private Date updatedOn;

	@CreatedBy
	@Column(name = "Updated_by")
	private String updatedBy;

	@Column(name = "Cancelled_by")
	private String cancelledBy;

	@Column(name = "Cancellation_reason")
	private String cancellationReason;

	@ManyToOne
	@JoinColumn(name = "spec_Id")
	private Speciality speciality;

}
