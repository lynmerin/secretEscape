package com.example.se.service.impl;

import com.example.se.model.Accounts;
import com.example.se.model.Transactions;
import com.example.se.repo.AccountRepo;
import com.example.se.repo.TransactionRepo;
import com.example.se.service.AccountService;
import com.example.se.specs.AccountSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepo accountRepo;

    @Autowired
    TransactionRepo transRepo;

    @Autowired
    AccountSpecs specs;

    /**
     * Predicate to check the mandatory fields in a transfer request body
     */
    Predicate<Transactions> checkMandatoryValues = transaction -> Objects.nonNull(transaction) && transaction.getFromAccountId()!= 0 && transaction.getToAccountId()!= 0 && transaction.getAmount() !=0.0;

    /**
     * To get all account details or to get account details of a specific account
     *
     * @param accountId
     * @return
     */
    @Override
    public List<Accounts> getAccounts(Long accountId) {
        return accountRepo.findAll(specs.getAccountSpec(accountId));
    }

    /**
     * To get all the Transactions happened in an account or to get a transaction of particular ID in an account
     *
     * @param accountId
     * @param transId
     * @return
     */
    @Override
    public List<Transactions> getAccountTransactions(Long accountId, Long transId) {
        if (Objects.nonNull(accountId)) {
            List<Transactions> transactions = transRepo.findByAccountId(accountId);
            if (Objects.nonNull(transId))
                transactions.removeIf(transaction -> transaction.getTransactionId() != transId.longValue());
            transactions.stream().filter(trax -> trax.getFromAccountId() == accountId.longValue()).forEach(trax -> trax.setTransactionType("DEBIT"));
            transactions.stream().filter(trax -> trax.getToAccountId() == accountId.longValue()).forEach(trax -> trax.setTransactionType("CREDIT"));
            return transactions;
        }
        return new ArrayList<>();
    }

    /**
     * To add a new Transfer transaction
     * @param transaction
     * @return
     */
    @Override
    public String addTransaction(Transactions transaction) {

        if (checkMandatoryValues.test(transaction)) {
            List<Accounts> fromAccounts = getAccounts(transaction.getFromAccountId());
            List<Accounts> toAccounts = getAccounts(transaction.getToAccountId());
            if (!CollectionUtils.isEmpty(fromAccounts)) {
                if (!CollectionUtils.isEmpty(toAccounts)) {
                    Accounts fromAccount = fromAccounts.get(0);
                    if (Objects.nonNull(fromAccount) && fromAccount.getBalance() >= transaction.getAmount()) {
                        return saveTransaction(transaction, fromAccount, toAccounts.get(0));
                    } else return "Transfer failed. Account doesn't have enough balance to perform the transfer.";
                } else return "Transfer failed. Invalid \"To\" account details.";

            } else return "Transfer failed. Invalid \"From\" account details.";

        } else
            return "Transfer failed. \"From\" account, \"To\" account and transaction amount is mandatory to proceed with the transfer.";
    }

    /**
     * To save the transfer and update the from and to account balance.
     * This is a transactional method to handle the rollback in case of exceptions.
     * @param transaction
     * @param fromAccount
     * @param toAccount
     * @return
     */
    @Transactional
    protected String saveTransaction(Transactions transaction, Accounts fromAccount, Accounts toAccount) {

        transaction.setTransactionId(transRepo.getNextId());
        transaction.setLastUpdateTime(LocalDateTime.now());
        try {
            fromAccount.setBalance(fromAccount.getBalance() - transaction.getAmount());
            toAccount.setBalance(toAccount.getBalance() + transaction.getAmount());
            transRepo.save(transaction);
            accountRepo.save(fromAccount);
            accountRepo.save(toAccount);
            return "Transfer completed successfully. Transaction Id : " + transaction.getTransactionId();
        } catch (Exception ex) {
            return "Transfer failed. Transaction reverted";
        }
    }
}
