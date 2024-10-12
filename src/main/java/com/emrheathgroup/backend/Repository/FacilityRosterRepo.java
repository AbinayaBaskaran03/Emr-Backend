package com.emrheathgroup.backend.Repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.FacilityRoster;

@Repository
public interface FacilityRosterRepo extends JpaRepository<FacilityRoster, Integer> {

	List<FacilityRoster> findByDate(Date date);

	@Query("SELECT DISTINCT fr FROM FacilityRoster fr WHERE fr.branchId.id = :branchId AND fr.date = :date")
	List<FacilityRoster> findDistinctFacilitiesByBranchAndDate(@Param("branchId") Integer branchId, @Param("date") Date date);


	@Query("SELECT COUNT(f) > 0 FROM FacilityRoster f WHERE f.rosterId = :rosterId AND f.date = :date AND f.status = :status AND "
			+ "(f.startTime < :endTime AND f.endTime > :startTime)")
	boolean existsByRosterIdAndDateAndStatusAndTimeOverlap(@Param("rosterId") Integer rosterId,
			@Param("date") Date date, @Param("status") boolean status, @Param("startTime") Time startTime,
			@Param("endTime") Time endTime);

	@Query("SELECT r FROM FacilityRoster r WHERE r.rosterId = :rosterId AND DATE(r.date) = :blockDate")
	Optional<FacilityRoster> findByRosterIdAndDate(@Param("rosterId") Integer rosterId,
			@Param("blockDate") Date blockDate);

}
