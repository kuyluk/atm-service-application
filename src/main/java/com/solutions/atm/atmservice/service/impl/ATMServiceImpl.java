package com.solutions.atm.atmservice.service.impl;

import com.solutions.atm.atmservice.data.ATMDatabase;
import com.solutions.atm.atmservice.exception.InvalidArgumentException;
import com.solutions.atm.atmservice.model.Banknote;
import com.solutions.atm.atmservice.model.Denomination;
import com.solutions.atm.atmservice.service.ATMService;
import com.solutions.atm.atmservice.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@Service
public class ATMServiceImpl implements ATMService {

    private static final Logger logger = LoggerFactory.getLogger(ATMServiceImpl.class);

    private static final int TWENTY = 20;
    private static final int TWO_FIFTY = 250;
    private static final int FIVE = 5;
    private static final int ZERO = 0;
    private static final int TEN = 10;

    private final AccountService accountService;

    private final ATMDatabase atmDatabase;

    public ATMServiceImpl(AccountService accountService, ATMDatabase atmDatabase) {
        this.accountService = accountService;
        this.atmDatabase = atmDatabase;
    }

    @Override
    public void replenish(final Denomination denomination) {

        logger.debug("replenish starts with denomination {} :", denomination);
        validateDenomination(denomination);
        atmDatabase.replenish(denomination.getFaceValue(), denomination.getQuantity());
        logger.debug("replenish ends.");
    }

    @Override
    public String checkBalance(final String accountId) {

        logger.debug("checkBalance starts with accountId : {}", accountId);
        String balance = accountService.retrieveAccountBalance(accountId);
        logger.debug("checkBalance ends with result : {}", balance);
        return balance;
    }

    @Override
    public Map<Banknote, Integer> withdrawAmountAndDisburse(final String accountId, final Integer amount) {

        logger.debug("withdrawAmountAndDisburse starts with accountId : {} and withdraw amount : {} ", accountId, amount);
        validateWithdrawAmount(amount);
        accountService.withdrawAmount(accountId, BigDecimal.valueOf(amount));

        Map<Banknote, Integer> banknoteMap = new HashMap<>();
        Integer changeAmount = includeBanknoteFive(amount, banknoteMap);

        for (Banknote banknote : Banknote.values()) {
            banknoteMap.compute(banknote, computeNumberOfNotes(changeAmount));
            changeAmount = changeAmount % banknote.noteValue();
        }

        banknoteMap.forEach((banknote, numberOfNotes) -> atmDatabase.updateNumberOfNotes(banknote.faceValue(), numberOfNotes));

        logger.debug("withdrawAmountAndDisburse ends with banknote map : {} ", banknoteMap);
        return banknoteMap;
    }

    private void validateWithdrawAmount(Integer amount) {
        if (amount < TWENTY) {
            throw new InvalidArgumentException("Withdraw amount can not be less than 20");
        }

        if (amount > TWO_FIFTY) {
            throw new InvalidArgumentException("Withdraw amount can not be greater than 250");
        }

        if (ZERO != amount % FIVE) {
            throw new InvalidArgumentException("Withdraw amount should be multiple of 5");
        }
    }

    private Integer includeBanknoteFive(final Integer amount, final Map<Banknote, Integer> banknoteMap) {
        if (FIVE != amount % TEN) {
            logger.trace("Five note is included by manually");
            banknoteMap.put(Banknote.FIVE, 1);
            return amount - FIVE;
        }
        logger.trace("Five note is not necessary to include by manually");
        return amount;
    }

    private void validateDenomination(Denomination denomination) {
        Optional.ofNullable(denomination)
                .orElseThrow(() -> new InvalidArgumentException("Denomination can not be null"));

        if (denomination.getQuantity() < 1) {
            throw new InvalidArgumentException("Denomination quantity can not be less than 1");
        }

        Arrays.stream(Banknote.values())
                .filter(b -> b.faceValue().equals(denomination.getFaceValue()))
                .findAny()
                .orElseThrow(() -> new InvalidArgumentException("Banknote is not acceptable"));
    }

    private BiFunction<Banknote, Integer, Integer> computeNumberOfNotes(Integer changeAmount) {

        logger.trace("computing the number of notes for amount : {}", changeAmount);
        return (banknote, numberOfBanknotes) -> {
            Integer banknoteQuantity = changeAmount / banknote.noteValue();
            if (numberOfBanknotes != null) {
                return numberOfBanknotes + banknoteQuantity;
            }
            logger.trace("Note quantity : {} for note value : {}", banknoteQuantity, banknote.noteValue());
            return banknoteQuantity;
        };
    }

}
