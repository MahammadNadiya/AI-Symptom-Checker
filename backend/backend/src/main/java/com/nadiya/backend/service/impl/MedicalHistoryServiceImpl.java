package com.nadiya.backend.service.impl;

import com.nadiya.backend.entity.MedicalHistory;
import com.nadiya.backend.repository.MedicalHistoryRepository;
import com.nadiya.backend.service.MedicalHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {

    private final MedicalHistoryRepository medicalHistoryRepository;

    public MedicalHistoryServiceImpl(MedicalHistoryRepository medicalHistoryRepository) {
        this.medicalHistoryRepository = medicalHistoryRepository;
    }

    @Override
    public MedicalHistory saveHistory(MedicalHistory history) {
        return medicalHistoryRepository.save(history);
    }

    @Override
    public List<MedicalHistory> getHistoryByUserId(Long userId) {
        return medicalHistoryRepository.findByUserId(userId);
    }
}