package com.example.se.service;

import com.example.se.model.Accounts;
import com.example.se.model.Transactions;
import com.example.se.repo.AccountRepo;
import com.example.se.repo.TransactionRepo;
import com.example.se.service.impl.AccountServiceImpl;
import com.example.se.specs.AccountSpecs;
import com.example.se.util.AccountTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import static org.mockito.Mockito.*;
import static  org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
public class AccountServiceTest  extends AccountTestHelper {
    @Mock
    AccountRepo acctRepo;

    @Mock
    AccountSpecs spec;

    @Mock
    TransactionRepo transRepo;

    @InjectMocks
    AccountServiceImpl service;

    List<Accounts> allAccount= new ArrayList<>();
    List<Transactions> allTrans= new ArrayList<>();
    @Before
    public void setup() {
        Accounts a1 = createNewAccount("a1", "abc@gmail.com", 1001);
        Accounts a2 = createNewAccount("a2", "cde@gmail.com", 1002);
        Accounts a3 = createNewAccount("a3", "fgh@gmail.com", 1003);
        Accounts a4 = createNewAccount("a4", "ijk@gmail.com", 1004);
        Transactions t1 = createTransaction(1001L, 1002L, 20.01, 1005, 1);
        Transactions t2 = createTransaction(1001L, 1002L, 50.50, 1006, 2);
        Transactions t3= createTransaction(1003L, 1001L , 100.50, 1007, 5);
        Transactions t4= createTransaction(1002L, 1003L , 10.50, 1008, 5);
        allAccount.add(a1);
        allAccount.add(a2);
        allAccount.add(a3);
        allAccount.add(a4);
        allTrans.add(t1);
        allTrans.add(t2);
        allTrans.add(t3);
        when(transRepo.findByAccountId(1001L)).thenReturn(allTrans);
    }
    @Test
    public void getAllAccountDetails(){
        when(acctRepo.findAll(spec.getAccountSpec(null))).thenReturn(allAccount);
        List<Accounts> accounts= service.getAccounts(null);
        assertFalse(CollectionUtils.isEmpty(accounts));
        assertEquals(accounts.size(), 4);
    }
    @Test
    public void getOneAccountDetails(){
        when(acctRepo.findAll(spec.getAccountSpec(1001L))).thenReturn(Arrays.asList(allAccount.get(0)));
        List<Accounts> accounts= service.getAccounts(1001L);
        assertFalse(CollectionUtils.isEmpty(accounts));
        assertEquals(accounts.size(), 1);
    }
    @Test
    public void getInvalidAccountDetails(){
        when(acctRepo.findAll(spec.getAccountSpec(1001L))).thenReturn(new ArrayList<>());
        List<Accounts> accounts= service.getAccounts(1021L);
        assertTrue(CollectionUtils.isEmpty(accounts));
    }
    @Test
    public void getAllTransactionForValidAccount(){
        List<Transactions> transactions= service.getAccountTransactions(1001L, null);
        assertFalse(CollectionUtils.isEmpty(transactions));
        assertEquals(transactions.size(), 3);
    }
    @Test
    public void getAllTransactionForInValidAccount(){
        List<Transactions> transactions= service.getAccountTransactions(1021L, null);
        assertTrue(CollectionUtils.isEmpty(transactions));
    }
    @Test
    public void getAllTransactionForValidAccountWhichDontHaveTransactions(){
        List<Transactions> transactions= service.getAccountTransactions(1004L, null);
        assertTrue(CollectionUtils.isEmpty(transactions));
    }
    @Test
    public void getOneTransactionForValidAccount(){
        List<Transactions> transactions= service.getAccountTransactions(1001L, 1005L);
        assertFalse(CollectionUtils.isEmpty(transactions));
        assertEquals(transactions.size(), 1);
    }
    @Test
    public void getOneTransactionForValidAccountWthTransIdOfDifferentAccount(){
        List<Transactions> transactions= service.getAccountTransactions(1001L, 1008L);
        assertTrue(CollectionUtils.isEmpty(transactions));
    }
    @Test
    public void getOneTransactionForInvalidAccount(){
        List<Transactions> transactions= service.getAccountTransactions(1021L, 1006L);
        assertTrue(CollectionUtils.isEmpty(transactions));
    }

    @Test
    public void newValidTransfer(){
        when(acctRepo.findAll(spec.getAccountSpec(1001L))).thenReturn(Arrays.asList(allAccount.get(0)));
        when(acctRepo.findAll(spec.getAccountSpec(1002L))).thenReturn(Arrays.asList(allAccount.get(1)));
        when(transRepo.getNextId()).thenReturn(1010L);
        Transactions transaction = createTransaction(1002L, 1001L, 21.21, 0,0);
        String response= service.addTransaction(transaction);
        assertFalse(StringUtils.isEmpty(response));
        assertEquals("Transfer completed successfully. Transaction Id : 1010", response);
    }

    @Test
    public void newTransferInvalidFromAcct(){
        when(acctRepo.findAll(spec.getAccountSpec(1001L))).thenReturn(Arrays.asList(allAccount.get(1)));
        when(acctRepo.findAll(spec.getAccountSpec(1008L))).thenReturn(new ArrayList<>());
        when(transRepo.getNextId()).thenReturn(1011L);
        Transactions transaction = createTransaction(1008L, 1001L, 21.21, 0,0);
        String response= service.addTransaction(transaction);
        assertFalse(StringUtils.isEmpty(response));
        assertEquals("Transfer failed. Invalid \"From\" account details.", response);
    }

    @Test
    public void newTransferWithNullFromAcct(){
        when(acctRepo.findAll(spec.getAccountSpec(1001L))).thenReturn(Arrays.asList(allAccount.get(0)));
        when(acctRepo.findAll(spec.getAccountSpec(1002L))).thenReturn(Arrays.asList(allAccount.get(1)));
        when(transRepo.getNextId()).thenReturn(1011L);
        Transactions transaction = createTransaction(0, 1001, 21.21, 0,0);
        String response= service.addTransaction(transaction);
        assertFalse(StringUtils.isEmpty(response));
        assertEquals("Transfer failed. \"From\" account, \"To\" account and transaction amount is mandatory to proceed with the transfer.", response);
    }

    @Test
    public void newTransferWithNullToAcct(){
        when(acctRepo.findAll(spec.getAccountSpec(1001L))).thenReturn(Arrays.asList(allAccount.get(0)));
        when(acctRepo.findAll(spec.getAccountSpec(1002L))).thenReturn(Arrays.asList(allAccount.get(1)));
        when(transRepo.getNextId()).thenReturn(1011L);
        Transactions transaction = createTransaction(1002, 0, 21.21, 0,0);
        String response= service.addTransaction(transaction);
        assertFalse(StringUtils.isEmpty(response));
        assertEquals("Transfer failed. \"From\" account, \"To\" account and transaction amount is mandatory to proceed with the transfer.", response);
    }

    @Test
    public void newTransferWithNullAmt(){
        when(acctRepo.findAll(spec.getAccountSpec(1001L))).thenReturn(Arrays.asList(allAccount.get(0)));
        when(acctRepo.findAll(spec.getAccountSpec(1002L))).thenReturn(Arrays.asList(allAccount.get(1)));
        when(transRepo.getNextId()).thenReturn(1011L);
        Transactions transaction = createTransaction(1002, 1001, 0, 0,0);
        String response= service.addTransaction(transaction);
        assertFalse(StringUtils.isEmpty(response));
        assertEquals("Transfer failed. \"From\" account, \"To\" account and transaction amount is mandatory to proceed with the transfer.", response);
    }
    @Test
    public void newTransferWithMoreAmountThanBalance(){
        when(acctRepo.findAll(spec.getAccountSpec(1001L))).thenReturn(Arrays.asList(allAccount.get(0)));
        when(acctRepo.findAll(spec.getAccountSpec(1002L))).thenReturn(Arrays.asList(allAccount.get(1)));
        when(transRepo.getNextId()).thenReturn(1011L);
        Transactions transaction = createTransaction(1002, 1001, 20000.30, 0,0);
        String response= service.addTransaction(transaction);
        assertFalse(StringUtils.isEmpty(response));
        assertEquals("Transfer failed. Account doesn't have enough balance to perform the transfer.", response);
    }
    @Test
    public void newValidTransferExactBalance(){
        when(acctRepo.findAll(spec.getAccountSpec(1001L))).thenReturn(Arrays.asList(allAccount.get(0)));
        when(acctRepo.findAll(spec.getAccountSpec(1002L))).thenReturn(Arrays.asList(allAccount.get(1)));
        when(transRepo.getNextId()).thenReturn(1010L);
        Transactions transaction = createTransaction(1002L, 1001L, 200, 0,0);
        String response= service.addTransaction(transaction);
        assertFalse(StringUtils.isEmpty(response));
        assertEquals("Transfer completed successfully. Transaction Id : 1010", response);
    }
    @Test
    public void newTransferWithMoreAmountThanBalance01(){
        when(acctRepo.findAll(spec.getAccountSpec(1001L))).thenReturn(Arrays.asList(allAccount.get(0)));
        when(acctRepo.findAll(spec.getAccountSpec(1002L))).thenReturn(Arrays.asList(allAccount.get(1)));
        when(transRepo.getNextId()).thenReturn(1011L);
        Transactions transaction = createTransaction(1002, 1001, 200.01, 0,0);
        String response= service.addTransaction(transaction);
        assertFalse(StringUtils.isEmpty(response));
        assertEquals("Transfer failed. Account doesn't have enough balance to perform the transfer.", response);
    }
}
