package com.solutions.atm.atmservice.service.impl;

import com.solutions.atm.atmservice.data.ATMDatabase;
import com.solutions.atm.atmservice.exception.InvalidArgumentException;
import com.solutions.atm.atmservice.model.Banknote;
import com.solutions.atm.atmservice.model.Denomination.DenominationBuilder;
import com.solutions.atm.atmservice.service.ATMService;
import com.solutions.atm.atmservice.service.AccountService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ATMServiceImplTest {

    private static final String ACCOUNT_ID = "01001";

    private ATMService atmService;

    @Mock
    private AccountService accountService;

    @Mock
    private ATMDatabase atmDatabase;

    @Rule
    public ExpectedException expectedExceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        atmService = new ATMServiceImpl(accountService, atmDatabase);
    }

    @Test
    public void replenish_shouldPutIntoDB_whenBanknoteIsValid() {
        atmService.replenish(DenominationBuilder.aDenomination()
                                                .withFaceValue(Banknote.FIVE.faceValue())
                                                .withQuantity(Integer.valueOf(10))
                                                .build());

        verify(atmDatabase).replenish(Banknote.FIVE.faceValue(), Integer.valueOf(10));
    }

    @Test
    public void replenish_shouldThrowException_whenBanknoteFaceIsNotValid() {
        expectedExceptionRule.expect(InvalidArgumentException.class);
        expectedExceptionRule.expectMessage("Banknote is not acceptable");

        atmService.replenish(DenominationBuilder.aDenomination()
                                                .withFaceValue("ONE")
                                                .withQuantity(Integer.valueOf(10))
                                                .build());
    }

    @Test
    public void replenish_shouldThrowException_whenDenominationQuantityIsNotValid() {
        expectedExceptionRule.expect(InvalidArgumentException.class);
        expectedExceptionRule.expectMessage("Denomination quantity can not be less than 1");

        atmService.replenish(DenominationBuilder.aDenomination()
                                                .withFaceValue(Banknote.TWENTY.faceValue())
                                                .withQuantity(Integer.valueOf(0))
                                                .build());
    }

    @Test
    public void replenish_shouldThrowException_whenDenominationIsNull() {
        expectedExceptionRule.expect(InvalidArgumentException.class);
        expectedExceptionRule.expectMessage("Denomination can not be null");

        atmService.replenish(null);
    }

    @Test
    public void checkBalance_shouldReturnFormattedString_WhenAccountIdIsValid() {

        String expected = "£2,354.78";
        when(accountService.retrieveAccountBalance(anyString())).thenReturn(expected);
        String actual = atmService.checkBalance(ACCOUNT_ID);

        assertThat(actual, is(equalTo(expected)));
        verify(accountService).retrieveAccountBalance(ACCOUNT_ID);
    }

    @Test
    public void withdrawAmountAndDisburse_shouldDisburseSmallestNumberOfNotes_WhenAmountIsValid() {

        Map<Banknote, Integer> actual = atmService.withdrawAmountAndDisburse(ACCOUNT_ID, 250);

        assertThat(actual.get(Banknote.FIVE), is(equalTo(2)));
        assertThat(actual.get(Banknote.TEN), is(equalTo(0)));
        assertThat(actual.get(Banknote.TWENTY), is(equalTo(2)));
        assertThat(actual.get(Banknote.FIFTY), is(equalTo(4)));

        verify(accountService).withdrawAmount(ACCOUNT_ID, BigDecimal.valueOf(250));
        verifyAtmDatabaseUpdatedWithCorrectNotes(actual);
    }

    @Test
    public void withdrawAmountAndDisburse_shouldDisburseSmallestNumberAndAllOfNotes_WhenAmountIsValid() {

        Map<Banknote, Integer> actual = atmService.withdrawAmountAndDisburse(ACCOUNT_ID, 235);

        assertThat(actual.get(Banknote.FIVE), is(equalTo(1)));
        assertThat(actual.get(Banknote.TEN), is(equalTo(1)));
        assertThat(actual.get(Banknote.TWENTY), is(equalTo(1)));
        assertThat(actual.get(Banknote.FIFTY), is(equalTo(4)));
        verify(accountService).withdrawAmount(ACCOUNT_ID, BigDecimal.valueOf(235));
        verifyAtmDatabaseUpdatedWithCorrectNotes(actual);
    }

    @Test
    public void withdrawAmountAndDisburse_shouldDisburseAtLeastOneFiveNote_WhenAmountIsValid() {

        Map<Banknote, Integer> actual = atmService.withdrawAmountAndDisburse(ACCOUNT_ID, 20);

        assertThat(actual.get(Banknote.FIVE), is(equalTo(2)));
        verify(accountService).withdrawAmount(ACCOUNT_ID, BigDecimal.valueOf(20));
    }

    @Test
    public void withdrawAmountAndDisburse_shouldThrowException_WhenAmountIsLessThan20() {

        expectedExceptionRule.expect(InvalidArgumentException.class);
        expectedExceptionRule.expectMessage("Withdraw amount can not be less than 20");

        atmService.withdrawAmountAndDisburse(ACCOUNT_ID, 19);
    }

    @Test
    public void withdrawAmountAndDisburse_shouldThrowException_WhenAmountIsMoreThan250() {

        expectedExceptionRule.expect(InvalidArgumentException.class);
        expectedExceptionRule.expectMessage("Withdraw amount can not be greater than 250");

        atmService.withdrawAmountAndDisburse(ACCOUNT_ID, 251);
    }

    @Test
    public void withdrawAmountAndDisburse_shouldThrowException_WhenAmountIsNotMultipleOf5() {

        expectedExceptionRule.expect(InvalidArgumentException.class);
        expectedExceptionRule.expectMessage("Withdraw amount should be multiple of 5");

        atmService.withdrawAmountAndDisburse(ACCOUNT_ID, 24);
    }


    private void verifyAtmDatabaseUpdatedWithCorrectNotes(Map<Banknote, Integer> actual) {
        actual.forEach((banknote, numberOfNotes) -> {
            verify(atmDatabase).updateNumberOfNotes(banknote.faceValue(), numberOfNotes);
        });
    }
}