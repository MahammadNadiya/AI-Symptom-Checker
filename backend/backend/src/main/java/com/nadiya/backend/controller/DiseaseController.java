package com.nadiya.backend.controller;

import com.nadiya.backend.dto.PredictionResult;
import com.nadiya.backend.entity.Disease;
import com.nadiya.backend.entity.MedicalHistory;
import com.nadiya.backend.entity.Symptom;
import com.nadiya.backend.entity.User;
import com.nadiya.backend.repository.UserRepository;
import com.nadiya.backend.service.DiseaseService;
import com.nadiya.backend.service.MedicalHistoryService;
import com.nadiya.backend.service.SymptomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/diseases")
@CrossOrigin("*")
public class DiseaseController {

    private final DiseaseService diseaseService;
    private final MedicalHistoryService medicalHistoryService;
    private final UserRepository userRepository;
    private final SymptomService symptomService;

    public DiseaseController(DiseaseService diseaseService,
                             MedicalHistoryService medicalHistoryService,
                             UserRepository userRepository,
                             SymptomService symptomService) {
        this.diseaseService = diseaseService;
        this.medicalHistoryService = medicalHistoryService;
        this.userRepository = userRepository;
        this.symptomService = symptomService;
    }

    @PostMapping
    public Disease saveDisease(@RequestBody Disease disease) {
        return diseaseService.saveDisease(disease);
    }

    @GetMapping
    public List<Disease> getAllDiseases() {
        return diseaseService.getAllDiseases();
    }

    @GetMapping("/{id}")
    public Disease getDiseaseById(@PathVariable Long id) {
        return diseaseService.getDiseaseById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDisease(@PathVariable Long id) {
        diseaseService.deleteDisease(id);
    }

    @PostMapping("/predict")
    public Map<String, Object> predict(@RequestBody Map<String, Object> request) {
        List<Integer> rawIds = (List<Integer>) request.get("symptomIds");
        List<Long> symptomIds = rawIds.stream().map(Integer::longValue).collect(Collectors.toList());

        String disclaimer = "This tool does not provide a medical diagnosis. Please consult a doctor for proper evaluation.";

        if (symptomIds.size() == 1) {
            Symptom symptom = symptomService.getSymptomById(symptomIds.get(0));

            boolean isUrgent = symptom != null && (
                    symptom.getName().toLowerCase().contains("severe")
                            || symptom.getName().toLowerCase().contains("reduced fetal movement")
                            || symptom.getDescription().toLowerCase().contains("prompt medical")
                            || symptom.getDescription().toLowerCase().contains("checked promptly")
                            || symptom.getDescription().toLowerCase().contains("immediately")
                            || symptom.getDescription().toLowerCase().contains("urgent")
            );

            String note;
            String advice;

            if (symptom == null) {
                note = "A single symptom alone usually isn't enough to point to a specific condition.";
                advice = "Add more symptoms for a more specific read.";
            } else if (isUrgent) {
                note = symptom.getName() + " on its own can be a warning sign. " + symptom.getDescription() + ".";
                advice = "This symptom alone may warrant prompt medical attention. Please consult a doctor or seek care rather than waiting.";
            } else {
                note = symptom.getName() + " on its own is usually mild and common. " + symptom.getDescription() + ".";
                advice = "Generally not a cause for concern on its own. Add more symptoms for a more specific read, or monitor and consult a doctor if it persists or worsens.";
            }

            return Map.of(
                    "disclaimer", disclaimer,
                    "riskAdvice", advice,
                    "infoNote", note,
                    "predictions", List.of()
            );
        }

        List<PredictionResult> predictions = diseaseService.predictDiseases(symptomIds);

        if (predictions.isEmpty()) {
            return Map.of(
                    "disclaimer", disclaimer,
                    "message", "No strong matches found. Try adding more symptoms, or consult a doctor if you're concerned.",
                    "predictions", predictions
            );
        }

        String topSeverity = predictions.get(0).getDisease().getSeverityLevel();
        String riskAdvice = switch (topSeverity) {
            case "Severe" -> "Your top match indicates a potentially serious condition. Please consult a doctor promptly.";
            case "Moderate" -> "Your top match suggests a moderate condition. Consider consulting a doctor if symptoms persist or worsen.";
            default -> "Your top match is generally mild. Self-care may help, but monitor your symptoms.";
        };

        if (request.get("userId") != null) {
            Long userId = Long.valueOf(request.get("userId").toString());
            User user = userRepository.findById(userId).orElse(null);

            if (user != null) {
                String symptomNames = predictions.get(0).getDisease().getSymptoms().stream()
                        .filter(s -> symptomIds.contains(s.getId()))
                        .map(s -> s.getName())
                        .collect(Collectors.joining(", "));

                MedicalHistory history = new MedicalHistory();
                history.setUser(user);
                history.setSelectedSymptoms(symptomNames);
                history.setPredictedDisease(predictions.get(0).getDisease().getName());
                history.setMatchPercentage(predictions.get(0).getMatchPercentage());

                medicalHistoryService.saveHistory(history);
            }
        }

        return Map.of(
                "disclaimer", disclaimer,
                "riskAdvice", riskAdvice,
                "predictions", predictions
        );
    }
}