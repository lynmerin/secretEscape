package com.example.se.util;

import com.example.se.model.Accounts;
import com.example.se.model.Transactions;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class AccountTestHelper {
    public static Accounts createNewAccount(String name, String email, int id) {
       Accounts account= new Accounts();
       account.setAccountId(id);
       account.setName(name);
       account.setEmailAddress(email);
       account.setBalance(200);
       return account;
    }


    public static Transactions createTransaction(long fromAcct, long toAcct, double amt, long pk, int days) {
        Transactions transactions = new Transactions();
        transactions.setFromAccountId(fromAcct);
        transactions.setToAccountId(toAcct);
        transactions.setAmount(amt);
        transactions.setTransactionId(pk);
        transactions.setLastUpdateTime(LocalDateTime.now().minusDays(days));
        return transactions;
    }

    public static String convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String result=mapper.writeValueAsString(object);
        return result;
    }


}
