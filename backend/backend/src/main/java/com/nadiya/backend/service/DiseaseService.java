package com.nadiya.backend.service;

import com.nadiya.backend.entity.Disease;
import com.nadiya.backend.dto.PredictionResult;
import java.util.List;

public interface DiseaseService {

    Disease saveDisease(Disease disease);

    List<Disease> getAllDiseases();

    Disease getDiseaseById(Long id);

    void deleteDisease(Long id);

    List<PredictionResult> predictDiseases(List<Long> symptomIds);
}