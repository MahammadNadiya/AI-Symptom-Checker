package com.nadiya.backend.service;

import com.nadiya.backend.entity.MedicalHistory;
import java.util.List;

public interface MedicalHistoryService {

    MedicalHistory saveHistory(MedicalHistory history);

    List<MedicalHistory> getHistoryByUserId(Long userId);
}