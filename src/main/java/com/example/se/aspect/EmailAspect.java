package com.example.se.aspect;

import com.example.se.model.Accounts;
import com.example.se.model.Transactions;
import com.example.se.repo.AccountRepo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class EmailAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    AccountRepo accountRepo;

    @Around(value = "execution(* com.example.se.controller.AccountController.doTransfer(..))")
    public String controllerAspectLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        String result;
       // Enable it only when the sending email have to be performed.
       // Transactions transactions = (Transactions) joinPoint.getArgs()[0];
        try {
            result = (String) joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Exception while handling transfer");
            throw throwable;
        }
        //Enable it only when the sending email have to be performed.
        //sendEmail(transactions, result);
        return result;
    }

    private void sendEmail(Transactions transactions, String result) {
        Optional<Accounts> fromAcctOptional= accountRepo.findById(transactions.getFromAccountId());
        Optional<Accounts> toAcctOptional= accountRepo.findById(transactions.getToAccountId());
        if(fromAcctOptional.isPresent()&& toAcctOptional.isPresent()) {
            setMessageDetails(fromAcctOptional, result);
            setMessageDetails(toAcctOptional, result);
        }
    }

    private void setMessageDetails(Optional<Accounts> acctOptional, String result ){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(acctOptional.get().getEmailAddress());
        msg.setText("Dear Customer,  \n"+result +"\n  Have a nice Day. \n Regards, ...");
        javaMailSender.send(msg);
    }
}
