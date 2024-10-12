package com.emrheathgroup.backend.Repository;

import java.sql.Time;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.FacilityRoster;
import com.emrheathgroup.backend.Entity.FacilityScheduling;

@Repository
public interface FacilitySchedulingRepo extends JpaRepository<FacilityScheduling, Integer> ,JpaSpecificationExecutor<FacilityScheduling>{

	boolean existsByRosterIdAndScheduleDateAndScheduleTime(FacilityRoster facRoster, Date scheduleDate,
			Time scheduleTime);

}
