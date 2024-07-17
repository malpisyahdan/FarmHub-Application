package com.project.app.farmhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.project.app.farmhub.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String>, JpaSpecificationExecutor<Payment> {

}