package com.nadiya.backend.controller;

import com.nadiya.backend.entity.MedicalHistory;
import com.nadiya.backend.service.MedicalHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
@CrossOrigin("*")
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;

    public MedicalHistoryController(MedicalHistoryService medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    @GetMapping("/{userId}")
    public List<MedicalHistory> getHistoryByUserId(@PathVariable Long userId) {
        return medicalHistoryService.getHistoryByUserId(userId);
    }
}