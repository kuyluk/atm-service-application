package com.solutions.atm.atmservice.data;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class ATMDatabase {

    public static final Map<String, Integer> BANKNOTES = new HashMap<>();
    public static final Map<String, BigDecimal> ACCOUNTS_WITH_BALANCE = new HashMap<>();

    public static void initializeAccounts(){
        ACCOUNTS_WITH_BALANCE.put("01001", BigDecimal.valueOf(2738.59));
        ACCOUNTS_WITH_BALANCE.put("01002", BigDecimal.valueOf(23.00));
        ACCOUNTS_WITH_BALANCE.put("01003", BigDecimal.valueOf(0.00));
    }

    public static void initializeATMDatabase(){
        BANKNOTES.put("FIVE", 100);
        BANKNOTES.put("TEN", 100);
        BANKNOTES.put("TWENTY", 100);
        BANKNOTES.put("FIFTY", 100);
    }

    public void updateNumberOfNotes(String faceValue, Integer numberOfNotes){
        BANKNOTES.compute(faceValue, (k, v) -> v - numberOfNotes);
    }

    public void replenish(String faceValue, Integer quantity){
        BANKNOTES.put(faceValue, quantity);
    }

    public BigDecimal retrieveBalance(String accountId) {
       return ACCOUNTS_WITH_BALANCE.get(accountId);
    }

    public void updateBalance(final String accountId, final BigDecimal amount){
        ACCOUNTS_WITH_BALANCE.put(accountId, amount);
    }

}
