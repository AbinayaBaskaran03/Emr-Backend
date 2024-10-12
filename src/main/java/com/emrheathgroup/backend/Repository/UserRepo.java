package com.emrheathgroup.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.UserTable;

@Repository
public interface UserRepo extends JpaRepository<UserTable, Long> {

	UserTable findByEmail(String email);

	UserTable findByUserName(String userName);

	UserTable findByPhoneNo(String phoneNo);

	UserTable findByResetToken(String token);

}
