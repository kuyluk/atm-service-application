package com.solutions.atm.atmservice.service.impl;

import com.solutions.atm.atmservice.data.ATMDatabase;
import com.solutions.atm.atmservice.exception.InvalidArgumentException;
import com.solutions.atm.atmservice.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final ATMDatabase atmDatabase;

    public AccountServiceImpl(ATMDatabase atmDatabase) {
        this.atmDatabase = atmDatabase;
    }

    @Override
    public String retrieveAccountBalance(final String accountId) {

        logger.debug("retrieveAccountBalance starts with accountId : {}", accountId);
        BigDecimal balance = retrieveBalanceAsDecimal(accountId);
        logger.debug("retrieveAccountBalance ends with decimal value of balance : {}", balance);
        return getFormattedDecimalString(balance);
    }

    @Override
    public void withdrawAmount(final String accountId, final BigDecimal amount) {

        logger.debug("withdrawAmount starts with accountId : {}, decimal value of amount : {}", accountId, amount);
        BigDecimal currentBalance = retrieveBalanceAsDecimal(accountId);
        if (currentBalance.compareTo(amount) < 0) {
            String message = String.format("Requested withdraw amount, %s, can not be greater than current balance, %s.",
                    getFormattedDecimalString(amount), getFormattedDecimalString(currentBalance));
            throw new InvalidArgumentException(message);
        }
        logger.debug("withdrawAmount ends.");
        atmDatabase.updateBalance(accountId, currentBalance.subtract(amount));
    }

    private BigDecimal retrieveBalanceAsDecimal(String accountId) {

        if (StringUtils.isEmpty(accountId)) {
            throw new InvalidArgumentException("Account id can not be null or empty");
        }

        BigDecimal balance = atmDatabase.retrieveBalance(accountId);
        Optional.ofNullable(balance)
                .orElseThrow(() -> new InvalidArgumentException("Account not found for given account id"));

        return balance;
    }

    private String getFormattedDecimalString(BigDecimal balance) {
        return NumberFormat.getCurrencyInstance(Locale.UK).format(balance);
    }
}
