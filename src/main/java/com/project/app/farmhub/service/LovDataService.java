package com.project.app.farmhub.service;

import java.util.List;
import java.util.Optional;

import com.project.app.farmhub.entity.LovData;
import com.project.app.farmhub.request.CreateLovDataRequest;
import com.project.app.farmhub.request.UpdateLovDataRequest;
import com.project.app.farmhub.response.LovDataResponse;

public interface LovDataService {

	void add(CreateLovDataRequest request);

	void edit(UpdateLovDataRequest request);

	void delete(String id);

	Optional<LovData> getEntityById(String id);

	LovDataResponse getById(String id);

	List<LovDataResponse> getAll();

	List<LovDataResponse> getLovs(String lovType, Boolean isActive);

}
