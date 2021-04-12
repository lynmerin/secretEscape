package com.example.se.service;

import com.example.se.model.Accounts;
import com.example.se.model.Transactions;

import java.util.List;

public interface AccountService {
    List<Accounts> getAccounts(Long accountId);
    List<Transactions> getAccountTransactions(Long accountId, Long transId);

    String addTransaction(Transactions transaction);
}
