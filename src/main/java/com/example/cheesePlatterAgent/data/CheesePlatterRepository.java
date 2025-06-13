package com.example.cheesePlatterAgent.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface CheesePlatterRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.userName = :userName")
    Optional<Customer> findCustomerByUserName(String userName);

    Optional<Customer> findCustomerById(Long userId);
}
