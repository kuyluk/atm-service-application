package com.solutions.atm.atmservice.service.impl;

import com.solutions.atm.atmservice.data.ATMDatabase;
import com.solutions.atm.atmservice.exception.InvalidArgumentException;
import com.solutions.atm.atmservice.service.AccountService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    public static final String ACCOUNT_ID = "01001";
    private AccountService accountService;

    @Mock
    private ATMDatabase atmDatabase;

    @Rule
    public ExpectedException expectedExceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        accountService = new AccountServiceImpl(atmDatabase);
    }

    @Test
    public void retrieveAccountBalance_shouldReturnFormattedAmount_WhenAccountIdIsValid() {

        when(atmDatabase.retrieveBalance(anyString())).thenReturn(BigDecimal.valueOf(2738.59));
        String actual = accountService.retrieveAccountBalance(ACCOUNT_ID);

        assertThat(actual, is(not(isEmptyOrNullString())));
        assertThat(actual, is(equalTo("£2,738.59")));
        verify(atmDatabase).retrieveBalance(ACCOUNT_ID);
    }

    @Test
    public void withdrawAmount_shouldSubtractAmountFromAccountBalance_WhenAccountIsValidAndHasEnoughMoney() {

        BigDecimal balance = BigDecimal.valueOf(2488.59);
        when(atmDatabase.retrieveBalance(anyString())).thenReturn(balance);
        BigDecimal withdrawAmount = BigDecimal.valueOf(250.00);
        accountService.withdrawAmount(ACCOUNT_ID, withdrawAmount);

        BigDecimal expectedSubtraction = balance.subtract(withdrawAmount);
        verify(atmDatabase).updateBalance(ACCOUNT_ID, expectedSubtraction);
    }

    @Test
    public void retrieveAccountBalance_shouldThrowException_WhenAccountIdIsNullOrEmpty() {

        expectedExceptionRule.expect(InvalidArgumentException.class);
        expectedExceptionRule.expectMessage("Account id can not be null or empty");

        accountService.retrieveAccountBalance("");
    }

    @Test
    public void retrieveAccountBalance_shouldThrowException_WhenAccountIsNotFound() {

        when(atmDatabase.retrieveBalance(anyString())).thenReturn(null);
        expectedExceptionRule.expect(InvalidArgumentException.class);
        expectedExceptionRule.expectMessage("Account not found for given account id");

        accountService.retrieveAccountBalance("abc");
    }

    @Test
    public void withdrawAmount_shouldThrowException_WhenAccountNotHaveEnoughMoney() {

        when(atmDatabase.retrieveBalance(anyString())).thenReturn(BigDecimal.valueOf(23.00));
        expectedExceptionRule.expect(InvalidArgumentException.class);
        expectedExceptionRule.expectMessage("Requested withdraw amount, £250.00, can not be greater than current balance, £23.00.");

        accountService.withdrawAmount("01002", BigDecimal.valueOf(250.00));
    }
}