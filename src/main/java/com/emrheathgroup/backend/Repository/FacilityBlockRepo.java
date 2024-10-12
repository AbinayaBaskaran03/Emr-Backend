package com.emrheathgroup.backend.Repository;

import java.sql.Time;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.FacilityBlock;

@Repository
public interface FacilityBlockRepo extends JpaRepository<FacilityBlock, Integer> {

	@Query("SELECT CASE WHEN COUNT(fb) > 0 THEN true ELSE false END " +
	           "FROM FacilityBlock fb " +
	           "WHERE fb.rosterId.id = :rosterId " +
	           "AND fb.blockDate = :scheduleDate " +
	           "AND ((fb.blockStartTime <= :endTime AND fb.blockEndTime >= :scheduleTime) " +
	           "OR (fb.blockStartTime <= :scheduleTime AND fb.blockEndTime >= :scheduleTime))")
	    boolean isTimeBlocked(@Param("rosterId") Integer rosterId,
	                          @Param("scheduleDate") Date scheduleDate,
	                          @Param("scheduleTime") Time scheduleTime,
	                          @Param("endTime") Time endTime);
}
