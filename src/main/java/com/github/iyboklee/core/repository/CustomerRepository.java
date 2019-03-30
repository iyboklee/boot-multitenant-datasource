/**
 * @Author iyboklee (iyboklee@gmail.com)
 */
package com.github.iyboklee.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.iyboklee.core.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}