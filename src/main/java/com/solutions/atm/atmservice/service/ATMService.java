package com.solutions.atm.atmservice.service;

import com.solutions.atm.atmservice.model.Banknote;
import com.solutions.atm.atmservice.model.Denomination;

import java.util.Map;

public interface ATMService {

    void replenish(Denomination denomination);

    String checkBalance(String accountId);

    Map<Banknote, Integer> withdrawAmountAndDisburse(String accountId, Integer amount);
}
