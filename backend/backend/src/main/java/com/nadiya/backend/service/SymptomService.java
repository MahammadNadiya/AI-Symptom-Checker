package com.nadiya.backend.service;

import com.nadiya.backend.entity.Symptom;
import java.util.List;

public interface SymptomService {

    Symptom saveSymptom(Symptom symptom);

    List<Symptom> getAllSymptoms();

    Symptom getSymptomById(Long id);

    void deleteSymptom(Long id);

    List<Symptom> searchSymptoms(String keyword);
}