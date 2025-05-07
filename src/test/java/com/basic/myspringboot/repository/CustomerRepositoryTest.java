package com.basic.myspringboot.repository;

import com.basic.myspringboot.entity.Customer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@DataJpaTest
@SpringBootTest
@Transactional
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    @Rollback(value = false)
    void testDeleteCustomer() {
        Customer customer = customerRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Customer Not Found."));
        customerRepository.delete(customer);
    }

    @Test
    @Rollback(value = false)
    void testUpdateCustomer() {
        Customer customer = customerRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Customer Not Found."));
        // 수정하려면 Entity 의 setter method 를 호출한다.
        customer.setCustomerName("홍길동");
        // Transaction 이 알아서 save 해줌 (@Transactional)
//        customerRepository.save(customer);
        assertThat(customer.getCustomerName()).isEqualTo("홍길동");
    }

    @Test
    void testNotFoundException() {
        Customer customer = customerRepository.findByCustomerId("A004")
                .orElseThrow(() -> new RuntimeException("Customer Not Found."));
//        assertThat(customer.getCustomerId()).isEqualTo("A001");

    }
    
    @Test
    void testFindBy() {
        Optional<Customer> byId = customerRepository.findById(1L);

//        assertThat(byId).isNotEmpty();

        if(byId.isPresent()) {
            Customer existCustomer = byId.get();
            assertThat(existCustomer.getId()).isEqualTo(1L);
        }

        Optional<Customer> optionalCustomer2 = customerRepository.findByCustomerId("A001");
        Customer a001Customer = optionalCustomer2.orElseGet(() -> new Customer());

        assertThat(a001Customer.getCustomerName()).isEqualTo("스프링");
//        assertThat(a001Customer.getCustomerName()).isNull();

        Customer notFoundCustomer = customerRepository.findByCustomerId("A004")
                .orElseGet(() -> new Customer());

        assertThat(notFoundCustomer.getCustomerName()).isNull();
    }


    @Test
    @Rollback(value = false)
    @Disabled
    void testCreateCustomer() throws Exception {
        // Given (준비 단계)
        Customer customer = new Customer();
        customer.setCustomerId("A003");
        customer.setCustomerName("스프링3");

        // When (실행 단계)
        Customer addCustomer = customerRepository.save(customer);

        // Then (검증 단계)
        assertThat(addCustomer).isNotNull();
        assertThat(addCustomer.getCustomerName()).isEqualTo("스프링3");

    }



}