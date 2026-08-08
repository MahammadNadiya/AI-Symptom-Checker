package com.nadiya.backend.service.impl;

import com.nadiya.backend.dto.PredictionResult;
import com.nadiya.backend.entity.Disease;
import com.nadiya.backend.repository.DiseaseRepository;
import com.nadiya.backend.service.DiseaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;

    private static final int MINIMUM_MATCHED_SYMPTOMS = 2;
    private static final double MINIMUM_MATCH_PERCENTAGE = 30.0;

    public DiseaseServiceImpl(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Override
    public Disease saveDisease(Disease disease) {
        return diseaseRepository.save(disease);
    }

    @Override
    public List<Disease> getAllDiseases() {
        return diseaseRepository.findAll();
    }

    @Override
    public Disease getDiseaseById(Long id) {
        return diseaseRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteDisease(Long id) {
        diseaseRepository.deleteById(id);
    }

    @Override
    public List<PredictionResult> predictDiseases(List<Long> symptomIds) {
        List<Disease> allDiseases = diseaseRepository.findAll();
        List<PredictionResult> results = new ArrayList<>();

        for (Disease disease : allDiseases) {
            long matchedCount = disease.getSymptoms().stream()
                    .filter(symptom -> symptomIds.contains(symptom.getId()))
                    .count();

            int totalSymptoms = disease.getSymptoms().size();

            if (matchedCount == 0) {
                continue;
            }

            PredictionResult result = new PredictionResult(disease, (int) matchedCount, totalSymptoms);

            if (result.getMatchedSymptomCount() >= MINIMUM_MATCHED_SYMPTOMS
                    && result.getMatchPercentage() >= MINIMUM_MATCH_PERCENTAGE) {
                results.add(result);
            }
        }

        results.sort(Comparator.comparingDouble(PredictionResult::getMatchPercentage).reversed());

        return results;
    }
}