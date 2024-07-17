package com.project.app.farmhub.service;

import java.util.List;
import java.util.Optional;

import com.project.app.farmhub.entity.Payment;
import com.project.app.farmhub.request.CreatePaymentRequest;
import com.project.app.farmhub.response.PaymentResponse;

public interface PaymentService {

	void add(CreatePaymentRequest request);

	void delete(String id);

	Optional<Payment> getEntityById(String id);

	PaymentResponse getById(String id);

	List<PaymentResponse> getAll();
}
