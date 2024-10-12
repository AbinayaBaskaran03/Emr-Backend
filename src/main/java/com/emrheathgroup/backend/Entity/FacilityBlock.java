package com.emrheathgroup.backend.Entity;

import java.sql.Time;
import java.util.Date;

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
@Table(name = "facility_block")
public class FacilityBlock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "block_id")
	private Integer blockId;

	@ManyToOne
	@JoinColumn(name = "roster_id")
	private FacilityRoster rosterId;

	@Column(name = "block_date", columnDefinition = "DATE")
	private Date blockDate;

	@Column(name = "block_start_time")
	private Time blockStartTime;

	@Column(name = "block_end_time")
	private Time blockEndTime;

	@Column(name = "title")
	private String title;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private Status status;

}
