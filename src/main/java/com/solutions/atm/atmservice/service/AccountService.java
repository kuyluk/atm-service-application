package com.solutions.atm.atmservice.service;

import java.math.BigDecimal;

public interface AccountService {

    String retrieveAccountBalance(String accountId);

    void withdrawAmount(String accountId, BigDecimal amount);
}
