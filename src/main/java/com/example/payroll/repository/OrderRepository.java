package com.example.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.payroll.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
