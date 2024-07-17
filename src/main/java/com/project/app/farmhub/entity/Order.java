package com.project.app.farmhub.entity;

import java.math.BigDecimal;

import com.project.app.farmhub.common.type.StatusOrder;
import com.project.app.farmhub.common.type.StatusPayment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends MasterEntity {

	@Column(name = "total_price")
	private BigDecimal totalPrice;

	@Column(name = "quantity")
	private Integer quantity;

	@ManyToOne
	@JoinColumn(name = "farmer_id")
	private User farmer;

	@ManyToOne
	@JoinColumn(name = "umkm_id")
	private User umkm;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Enumerated(value = EnumType.STRING)
	private StatusOrder status;
	
	@Enumerated(value = EnumType.STRING)
	private StatusPayment statusPayment;
}
