package com.example.se.controller;

import com.example.se.model.Accounts;
import com.example.se.model.Transactions;
import com.example.se.repo.TransactionRepo;
import com.example.se.service.impl.AccountServiceImpl;
import com.example.se.util.AccountTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest extends AccountTestHelper {

    @MockBean
    private AccountServiceImpl service;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    AccountController controller;

    @Before
    public void setup(){
        Accounts a1 = createNewAccount("a1", "abc@gmail.com", 1001);
        Accounts a2 = createNewAccount("a2", "cde@gmail.com", 1002);
        Accounts a3 = createNewAccount("a3", "fgh@gmail.com",1003);
        Transactions t1= createTransaction(1001L, 1002L , 20.01, 1004, 1);
        Transactions t2= createTransaction(1001L, 1002L , 50.50, 1005, 2);
        Transactions t3= createTransaction(1003L, 1001L , 100.50, 1006, 5);
        when(service.getAccounts(null)).thenReturn(Arrays.asList(a1,a2,a3));
        when(service.getAccounts(1001L)).thenReturn(Arrays.asList(a1));
        when(service.getAccounts(1002L)).thenReturn(Arrays.asList(a2));
        when(service.getAccountTransactions(1001L, null)).thenReturn(Arrays.asList(t1,t2,t3));
        when(service.getAccountTransactions(1001L, 1004L)).thenReturn(Arrays.asList(t1));
    }

    @Test
    public void getAllAccountsTest() throws Exception {
        mockMvc.perform(get("/accounts").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].AccountId").value("1001"))
                .andExpect(jsonPath("$.[1].AccountId").value("1002"))
                .andExpect(jsonPath("$.[2].AccountId").value("1003"));
    }

    @Test
    public void getOneAccountIdTest() throws Exception {
        mockMvc.perform(get("/accounts/1001").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].AccountId").value("1001"));
    }
    @Test
    public void getOneAccountIdInvalidTest() throws Exception {
        mockMvc.perform(get("/accounts/1021").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getOneAccountIdsTransactionsTest() throws Exception {
        mockMvc.perform(get("/accounts/1001/transactions").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].TransactionId").value("1004"))
                .andExpect(jsonPath("$.[1].TransactionId").value("1005"))
                .andExpect(jsonPath("$.[2].TransactionId").value("1006"));
    }

    @Test
    public void getOneAccountIdsOneTransactionTest() throws Exception {
        mockMvc.perform(get("/accounts/1001/transactions/1004").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].TransactionId").value("1004"));
    }

    @Test
    public void getOneAccountIdsInvalidTransactionIdTest() throws Exception {
        mockMvc.perform(get("/accounts/1001/transactions/1024").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
    @Test
    public void getInvalidAccountIdWithValidTransactionIdTest() throws Exception {
        mockMvc.perform(get("/accounts/1021/transactions/1004").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getWithNullAccountIdForGettingTransactions() throws Exception {
        mockMvc.perform(get("/accounts/transactions").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void getWithInvalidLongAccountId() throws Exception {
        mockMvc.perform(get("/accounts/abc/transactions").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getWithInvalidPath() throws Exception {
        mockMvc.perform(get("/account/1001/transactions").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void doTransferValidTest() throws Exception{
        Transactions trans= createTransaction(1001,1002,34.21,0, 0);
        trans.setLastUpdateTime(null);
        when(service.addTransaction(trans)).thenReturn("Transfer completed successfully. Transaction Id : 1007");
        mockMvc.perform(post("/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(trans)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer completed successfully. Transaction Id : 1007"));
    }
}
