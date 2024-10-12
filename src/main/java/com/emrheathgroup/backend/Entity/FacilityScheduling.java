package com.emrheathgroup.backend.Entity;

import java.sql.Time;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.emrheathgroup.backend.Enumeration.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "facility_scheduling")
public class FacilityScheduling {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "roster_id")
	private FacilityRoster rosterId;

	@ManyToOne
	@JoinColumn(name = "doctor_id")
	private Doctor doctorId;

	@Column(name = "schedule_date")
	private Date scheduleDate;

	@Column(name = "schedule_time")
	private Time scheduleTime;

	@Column(name = "end_time")
	private Time endTime;

	@Column(name = "schedule_type")
	private String scheduleType;

	@Column(name = "schedule_status")
	private Boolean scheduleStatus;

	@CreationTimestamp
	@Column(name = "created_on")
	private Date createdOn;

	@CreatedBy
	@Column(name = "created_by")
	private String createdBy;

	@UpdateTimestamp
	@Column(name = "updated_on")
	private Date updatedOn;

	@LastModifiedBy
	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private Status status;

}
