package com.github.iyboklee;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.iyboklee.core.model.Customer;
import com.github.iyboklee.core.model.Email;
import com.github.iyboklee.core.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CustomerServiceTest {

    @Autowired private CustomerService customerService;

    @Test
    public void findOneTest() {
        Customer customer = customerService.findOne(1L);
        Assert.assertNotNull(customer);
        Assert.assertEquals(new Email("tester1@gmail.com"), customer.getEmail());
        log.info("{}", customer);
    }

    @Test
    public void findOneWithReadOnlyTest() {
        Customer customer = customerService.findOneWithReadOnly(1L);
        Assert.assertNotNull(customer);
        Assert.assertEquals(new Email("tester1@hotmail.com"), customer.getEmail());
        log.info("{}", customer);
    }

}