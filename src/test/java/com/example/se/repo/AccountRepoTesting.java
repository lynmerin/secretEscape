package com.example.se.repo;

import com.example.se.model.Accounts;
import com.example.se.specs.AccountSpecs;
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
public class AccountRepoTesting {

    @Autowired
    public AccountRepo repo;

    AccountSpecs spec= new AccountSpecs();

    @Test
    public void getAllAcountsTest(){
        List<Accounts> accounts= repo.findAll(spec.getAccountSpec(null));
        assertFalse(CollectionUtils.isEmpty(accounts));
        assertEquals(accounts.size(), 5);
    }
    @Test
    public void getOneAcountTest(){
        List<Accounts> accounts= repo.findAll(spec.getAccountSpec(100L));
        assertFalse(CollectionUtils.isEmpty(accounts));
        assertEquals(accounts.size(), 1);
    }
    @Test
    public void getInvalidAcountTest(){
        List<Accounts> accounts= repo.findAll(spec.getAccountSpec(200L));
        assertTrue(CollectionUtils.isEmpty(accounts));
    }

    @Test
    public void getAllAcountsSortedTest(){
        List<Accounts> accounts= repo.findAll(spec.getAccountSpec(null));
        assertFalse(CollectionUtils.isEmpty(accounts));
        assertEquals(accounts.size(), 5);
        assertEquals(accounts.get(0).getName(), "account1");
        assertEquals(accounts.get(4).getName(), "account5");
    }
}
