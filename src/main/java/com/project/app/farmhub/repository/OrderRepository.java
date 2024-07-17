package com.project.app.farmhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.project.app.farmhub.entity.Order;

public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

}