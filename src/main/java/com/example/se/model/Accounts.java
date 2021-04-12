package com.example.se.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Data
@Entity
public class Accounts {
    @Id
    @Column(name= "ACCOUNT_ID")
    @JsonProperty("AccountId")
    long accountId;
    @Column(name= "NAME")
    @JsonProperty("AccountName")
    String name;
    @Column(name= "BALANCE")
    @JsonProperty("Balance")
    double balance;
    @Column(name= "EMAIL")
    @JsonProperty("Email")
    String emailAddress;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accounts that = (Accounts) o;
        return accountId == that.accountId &&
                name == that.name &&
                emailAddress == that.emailAddress &&
                Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, name, emailAddress, balance);
    }
}
