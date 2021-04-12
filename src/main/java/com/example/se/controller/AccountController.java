package com.example.se.controller;

import com.example.se.model.Accounts;
import com.example.se.model.Transactions;
import com.example.se.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService service;

    /**
     * To expose the account details using HTTP GET method
     * @param accountId
     * @return
     */
    @GetMapping(value = {"/{accountId}", ""})
    public List<Accounts> getAccountDetails(@PathVariable(required = false) Long accountId ){
        return service.getAccounts(accountId);
    }

    /**
     *  To expose the transfer details in an account using HTTP GET method
     * @param accountId
     * @param transId
     * @return
     */
    @GetMapping(value = {"/{accountId}/transactions", "/{accountId}/transactions/{transId}"})
    public List<Transactions> getAccountTransactions(@PathVariable Long accountId , @PathVariable(required = false) Long transId ){
        return service.getAccountTransactions(accountId, transId);
    }

    /**
     *  To allow a new transfer using HTTP POST method
     * @param transaction
     * @return
     */
    @PostMapping("/transfer")
    public String getAccountTransactions( @RequestBody Transactions transaction ){
        return service.addTransaction(transaction);
    }
}
