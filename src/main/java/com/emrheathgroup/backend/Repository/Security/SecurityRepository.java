package com.emrheathgroup.backend.Repository.Security;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.emrheathgroup.backend.Entity.ItemKeys;

public interface SecurityRepository extends JpaRepository<ItemKeys, Integer> {

	@Query("SELECT name as name, value as value from ItemKeys")
	List<Map<String,String>> getItemKeysValue();


}
