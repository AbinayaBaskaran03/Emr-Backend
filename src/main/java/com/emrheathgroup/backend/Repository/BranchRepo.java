package com.emrheathgroup.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.BranchMaster;

@Repository
public interface BranchRepo extends JpaRepository<BranchMaster, Integer> {

    @Query(value = "Select bm from BranchMaster bm where lower(bm.branchName) = :branchName")
    BranchMaster findByBranchName(@Param("branchName") String branchName);

    List<BranchMaster> findAllByIsDeletedFalseAndIsActiveTrue();
}
