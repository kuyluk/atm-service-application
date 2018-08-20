package com.solutions.atm.atmservice;

import com.solutions.atm.atmservice.service.impl.ATMServiceImpl;
import com.solutions.atm.atmservice.service.impl.AccountServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AtmServiceApplicationTests {

    @Autowired
    private ApplicationContext context;


	@Test
	public void main_shouldLoadAllBeansIntoContext() {
        AccountServiceImpl accountService = context.getBean(AccountServiceImpl.class);
        ATMServiceImpl atmService = context.getBean(ATMServiceImpl.class);

        assertThat(accountService, is(notNullValue()));
        assertThat(atmService, is(notNullValue()));
    }

}
