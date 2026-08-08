package com.nadiya.backend.service.impl;

import com.nadiya.backend.entity.Symptom;
import com.nadiya.backend.repository.SymptomRepository;
import com.nadiya.backend.service.SymptomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SymptomServiceImpl implements SymptomService {

    private final SymptomRepository symptomRepository;

    public SymptomServiceImpl(SymptomRepository symptomRepository) {
        this.symptomRepository = symptomRepository;
    }

    @Override
    public Symptom saveSymptom(Symptom symptom) {
        return symptomRepository.save(symptom);
    }

    @Override
    public List<Symptom> getAllSymptoms() {
        return symptomRepository.findAll();
    }

    @Override
    public Symptom getSymptomById(Long id) {
        return symptomRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteSymptom(Long id) {
        symptomRepository.deleteById(id);
    }

    @Override
    public List<Symptom> searchSymptoms(String keyword) {
        return symptomRepository.findByNameContainingIgnoreCase(keyword);
    }
}