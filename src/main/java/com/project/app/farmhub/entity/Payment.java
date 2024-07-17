package com.project.app.farmhub.entity;

import com.project.app.farmhub.common.type.StatusPayment;

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
@Table(name = "t_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends MasterEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lov_payment_method_id")
	private LovData lovPaymentMethod;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Enumerated(value = EnumType.STRING)
	private StatusPayment statusPayment;

}
