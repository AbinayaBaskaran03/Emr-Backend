package com.emrheathgroup.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.RoleMaster;

@Repository
public interface RoleRepo extends JpaRepository<RoleMaster, Integer> {

    @Query(value = "Select rm from RoleMaster rm where lower(rm.roleName) = :role")
    RoleMaster findByRoleName(@Param("role") String roleName);
}
