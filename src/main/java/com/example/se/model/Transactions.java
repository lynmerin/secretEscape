package com.example.se.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
public class Transactions {
    @Id
    @Column(name= "TRANSACTION_ID")
    @JsonProperty("TransactionId")
    long transactionId;
    @Column(name= "FROM_ACCOUNT_ID")
    @JsonProperty("FromAccountId")
    long fromAccountId;
    @Column(name= "TO_ACCOUNT_ID")
    @JsonProperty("ToAccountId")
    long toAccountId;
    @Column(name= "AMOUNT")
    @JsonProperty("Amount")
    double amount;
    @Column(name= "LAST_UPDATE_TIME")
    @JsonProperty("LastUpdateTime")
    LocalDateTime lastUpdateTime;
    @Transient
    @JsonProperty("Type")
    String transactionType;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transactions that = (Transactions) o;
        return transactionId == that.transactionId &&
                fromAccountId == that.fromAccountId &&
                toAccountId == that.toAccountId &&
                Objects.equals(amount, that.amount)&&
                Objects.equals(lastUpdateTime, that.lastUpdateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, fromAccountId, toAccountId, amount,lastUpdateTime);
    }

}
