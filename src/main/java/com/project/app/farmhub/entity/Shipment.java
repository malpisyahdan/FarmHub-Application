package com.project.app.farmhub.entity;

import com.project.app.farmhub.common.type.StatusShipment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_shipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shipment extends MasterEntity {

	@Column(name = "tracking_number")
	private String trackingNumber;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lov_courier_service_id")
	private LovData lovCourierService;

	@Enumerated(value = EnumType.STRING)
	private StatusShipment statusShipment;

}
