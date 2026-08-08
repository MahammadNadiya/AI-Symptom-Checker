package com.nadiya.backend.controller;

import com.nadiya.backend.entity.Symptom;
import com.nadiya.backend.service.SymptomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/symptoms")
@CrossOrigin("*")
public class SymptomController {

    private final SymptomService symptomService;

    public SymptomController(SymptomService symptomService) {
        this.symptomService = symptomService;
    }

    @PostMapping
    public Symptom saveSymptom(@RequestBody Symptom symptom) {
        return symptomService.saveSymptom(symptom);
    }

    @GetMapping
    public List<Symptom> getAllSymptoms() {
        return symptomService.getAllSymptoms();
    }

    @GetMapping("/{id}")
    public Symptom getSymptomById(@PathVariable Long id) {
        return symptomService.getSymptomById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteSymptom(@PathVariable Long id) {
        symptomService.deleteSymptom(id);
    }

    @GetMapping("/search")
    public List<Symptom> searchSymptoms(@RequestParam String keyword) {
        return symptomService.searchSymptoms(keyword);
    }
}