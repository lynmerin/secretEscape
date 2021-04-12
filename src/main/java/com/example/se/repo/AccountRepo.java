package com.example.se.repo;

import com.example.se.model.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountRepo extends JpaRepository<Accounts, Long>, JpaSpecificationExecutor<Accounts> {

}
