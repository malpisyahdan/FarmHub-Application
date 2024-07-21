package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

import com.project.app.farmhub.entity.LovData;

public interface LovDataRepository extends MasterRepository<LovData, String> {
	boolean existsByLovTypeAndCode(String lovType, String code);

	Optional<LovData> findByLovTypeAndCode(String lovType, String code);

	List<LovData> findByLovTypeAndIsActive(String lovType, Boolean isActive);

	List<LovData> findByLovType(String lovType);
}
