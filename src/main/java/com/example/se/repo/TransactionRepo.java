package com.example.se.repo;

import com.example.se.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transactions, Long>{

    @Query("Select t from Transactions t where t.fromAccountId = :accountId or t.toAccountId= :accountId order by t.lastUpdateTime desc ")
    List<Transactions> findByAccountId(long accountId);

    @Query(
            value = "select nextval('se.PK_GENERATOR')",
            nativeQuery = true
    )
    Long getNextId();
}
