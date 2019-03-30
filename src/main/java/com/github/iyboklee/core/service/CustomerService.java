/**
 * @Author iyboklee (iyboklee@gmail.com)
 */
package com.github.iyboklee.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.iyboklee.core.model.Customer;
import com.github.iyboklee.core.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired private CustomerRepository customerRepository;

    @Transactional
    public Customer findOne(Long id) {
        return customerRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Customer findOneWithReadOnly(Long id) {
        return customerRepository.findOne(id);
    }

}