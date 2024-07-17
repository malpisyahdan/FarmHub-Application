package com.project.app.farmhub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.project.app.farmhub.entity.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, String>, JpaSpecificationExecutor<Shipment> {
	Optional<Shipment> findByOrderId(String orderId);
}