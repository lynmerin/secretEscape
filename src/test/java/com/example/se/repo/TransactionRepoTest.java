package com.example.se.repo;

import com.example.se.model.Transactions;
import com.example.se.util.AccountTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionRepoTest extends AccountTestHelper {
    @Autowired
    TransactionRepo repo;

    @Test
    public void getAllTransactionsTest() {
        List<Transactions> transactionsList = repo.findByAccountId(100L);
        assertTrue(CollectionUtils.isEmpty(transactionsList));
        Transactions t1 = createTransaction(100, 101, 20.21, 110, 0);
        repo.save(t1);
        transactionsList = repo.findByAccountId(100L);
        assertFalse(CollectionUtils.isEmpty(transactionsList));
        assertEquals(transactionsList.size(), 1);
    }

    @Test
    public void getTransactionsforInvalidAccountId(){
        List<Transactions> transactionsList = repo.findByAccountId(200L);
        assertTrue(CollectionUtils.isEmpty(transactionsList));
    }

}
