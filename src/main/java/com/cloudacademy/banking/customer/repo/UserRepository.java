package com.cloudacademy.banking.customer.repo;

import com.cloudacademy.banking.customer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}