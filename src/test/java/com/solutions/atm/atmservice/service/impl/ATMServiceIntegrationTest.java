package com.solutions.atm.atmservice.service.impl;

import com.solutions.atm.atmservice.data.ATMDatabase;
import com.solutions.atm.atmservice.model.Banknote;
import com.solutions.atm.atmservice.model.Denomination;
import com.solutions.atm.atmservice.service.ATMService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Map;

import static com.solutions.atm.atmservice.model.Denomination.DenominationBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ATMServiceIntegrationTest {

    @Autowired
    private ATMService atmService;

    @Test
    public void checkBalance_shouldReturnUKCurrencyFormattedBalance() {

        ATMDatabase.initializeAccounts();

        String actualBalance = atmService.checkBalance("01001");

        assertThat(actualBalance, is(equalTo("£2,738.59")));
    }

    @Test
    public void withdrawAmountAndDisburse_shouldReturnNotesOfCorrectDenominations() {

        ATMDatabase.initializeATMDatabase();
        ATMDatabase.initializeAccounts();

        Map<Banknote, Integer> actualNotes = atmService.withdrawAmountAndDisburse("01001", 85);

        String actualBalance = atmService.checkBalance("01001");

        assertThat(actualBalance, is(equalTo("£2,653.59")));
        assertThat(actualNotes.get(Banknote.FIVE), is(equalTo(1)));
        assertThat(actualNotes.get(Banknote.TEN), is(equalTo(1)));
        assertThat(actualNotes.get(Banknote.TWENTY), is(equalTo(1)));
        assertThat(actualNotes.get(Banknote.FIFTY), is(equalTo(1)));

        Arrays.stream(Banknote.values()).forEach(note -> {
            Integer currentNumberOfNotes = ATMDatabase.BANKNOTES.get(note.faceValue());
            assertThat(currentNumberOfNotes, is(equalTo(99)));
        });
    }

    @Test
    public void replenish_shouldFillDatabaseUpWithNotes() {

        Denomination fiveNotesDenomination = DenominationBuilder.aDenomination()
                .withQuantity(100)
                .withFaceValue(Banknote.FIVE.faceValue())
                .build();

        Denomination tenNotesDenomination = DenominationBuilder.aDenomination()
                .withQuantity(100)
                .withFaceValue(Banknote.TEN.faceValue())
                .build();
        Denomination twentyNotesDenomination = DenominationBuilder.aDenomination()
                .withQuantity(100)
                .withFaceValue(Banknote.TWENTY.faceValue())
                .build();
        Denomination fiftyNotesDenomination = DenominationBuilder.aDenomination()
                .withQuantity(100)
                .withFaceValue(Banknote.FIFTY.faceValue())
                .build();

        atmService.replenish(fiveNotesDenomination);
        atmService.replenish(tenNotesDenomination);
        atmService.replenish(twentyNotesDenomination);
        atmService.replenish(fiftyNotesDenomination);

        assertThat(ATMDatabase.BANKNOTES, hasEntry(Banknote.FIVE.faceValue(), 100));
        assertThat(ATMDatabase.BANKNOTES, hasEntry(Banknote.TEN.faceValue(), 100));
        assertThat(ATMDatabase.BANKNOTES, hasEntry(Banknote.TWENTY.faceValue(), 100));
        assertThat(ATMDatabase.BANKNOTES, hasEntry(Banknote.FIFTY.faceValue(), 100));
    }

}
