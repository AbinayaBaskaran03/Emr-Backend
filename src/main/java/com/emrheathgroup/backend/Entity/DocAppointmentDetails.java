package com.emrheathgroup.backend.Entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doc_appoint_details")
public class DocAppointmentDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "appoint_detail_id")
	private Integer appointDetailId;

	@OneToOne
	@JoinColumn(name = "appointment_id")
	private DoctorAppointment appointmentId;

	@Column(name = "appoint_type")
	private String appointType;

	@Column(name = "reason")
	private String reason;

	@Column(name = "meeting_type")
	private String meetingType;

	@Lob
	@Column(name = "attachment", columnDefinition = "LONGBLOB")
	private byte[] attachment;

	@CreationTimestamp
	@Column(name = "created_on")
	private Date createdOn;

	@CreatedBy
	@Column(name = "created_by")
	private String createdBy;
}
