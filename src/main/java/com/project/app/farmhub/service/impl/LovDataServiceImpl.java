package com.project.app.farmhub.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.app.farmhub.entity.LovData;
import com.project.app.farmhub.helper.SecurityHelper;
import com.project.app.farmhub.repository.LovDataRepository;
import com.project.app.farmhub.request.CreateLovDataRequest;
import com.project.app.farmhub.request.UpdateLovDataRequest;
import com.project.app.farmhub.response.LovDataResponse;
import com.project.app.farmhub.service.LovDataService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LovDataServiceImpl implements LovDataService {

	private final LovDataRepository repository;

	@Transactional
	@Override
	public void add(CreateLovDataRequest request) {
		validateRole("add");
		validateBkNotExist(request.getLovType(), request.getCode());
		LovData entity = mapToEntity(request);
		repository.saveAndFlush(entity);

	}

	private LovData mapToEntity(CreateLovDataRequest request) {
		LovData lov = new LovData();
		lov.setCode(request.getCode());
		lov.setName(request.getName());
		lov.setOrdNumber(request.getOrdNumber());
		lov.setLovType(request.getLovType());
		lov.setIsActive(true);
		return lov;
	}

	public void validateBkNotExist(String lovType, String code) {
		if (repository.existsByLovTypeAndCode(lovType, code)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "lov is exist.");
		}
	}
	
	private void validateRole(String action) {
		if (!SecurityHelper.hasRole("ADMIN")) {
			throw new IllegalArgumentException("Only users with role ADMIN can " + action + " lov data.");
		}

	}

	@Transactional
	@Override
	public void edit(UpdateLovDataRequest request) {
		validateRole("edit");
		validateIdExist(request.getId());
		LovData lov = getEntityById(request.getId()).orElse(null);
		validateBkNotChange(lov, request.getLovType(), request.getCode());
		lov = mapToEntity(lov, request);
		repository.saveAndFlush(lov);

	}

	public void validateIdExist(String id) {
		if (!repository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "lov is not exist.");
		}
	}

	public LovData mapToEntity(LovData lov, UpdateLovDataRequest request) {
		lov.setId(request.getId());
		lov.setCode(request.getCode());
		lov.setName(request.getName());
		lov.setOrdNumber(request.getOrdNumber());
		lov.setLovType(request.getLovType());
		lov.setIsActive(request.getIsActive());

		return lov;
	}

	public void validateBkNotChange(LovData entity, String lovType, String code) {
		if (!entity.getLovType().equals(lovType)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "lov type cannot be change.");
		}
		if (!entity.getCode().equals(code)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "code cannot be change.");
		}
	}

	@Override
	public void delete(String id) {
		validateRole("delete");
		validateIdExist(id);
		repository.deleteById(id);

	}

	@Override
	public Optional<LovData> getEntityById(String id) {
		return repository.findById(id);
	}

	@Override
	public LovDataResponse getById(String id) {
		LovData entity = getEntityById(id).orElse(null);
		return mapToResponse(entity);
	}

	@Override
	public List<LovDataResponse> getAll() {
		List<LovData> detail = repository.findAll();
		return detail.stream().map(this::mapToResponse).toList();
	}

	@Override
	public List<LovDataResponse> getLovs(String lovType, Boolean isActive) {
		List<LovData> details;

		if (isActive != null) {
			details = repository.findByLovTypeAndIsActive(lovType, isActive);
		} else {
			details = repository.findByLovType(lovType);
		}

		return details.stream().map(this::mapToResponse).toList();
	}

	public LovDataResponse mapToResponse(LovData entity) {
		LovDataResponse response = new LovDataResponse();
		if (entity != null) {
			response.setId(entity.getId());
			response.setOrdNumber(entity.getOrdNumber());
			response.setCode(entity.getCode());
			response.setName(entity.getName());
			response.setLovType(entity.getLovType());
			response.setIsActive(entity.getIsActive());
			response.setCreatedAt(entity.getCreatedAt());
			response.setUpdatedAt(entity.getUpdatedAt());
			response.setVersion(entity.getVersion());
		}
		return response;
	}

}
