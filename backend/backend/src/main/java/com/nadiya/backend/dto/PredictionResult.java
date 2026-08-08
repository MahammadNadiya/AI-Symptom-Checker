package com.nadiya.backend.dto;

import com.nadiya.backend.entity.Disease;

public class PredictionResult {

    private Disease disease;
    private int matchedSymptomCount;
    private int totalDiseaseSymptoms;
    private double matchPercentage;

    public PredictionResult(Disease disease, int matchedSymptomCount, int totalDiseaseSymptoms) {
        this.disease = disease;
        this.matchedSymptomCount = matchedSymptomCount;
        this.totalDiseaseSymptoms = totalDiseaseSymptoms;
        this.matchPercentage = totalDiseaseSymptoms == 0 ? 0 :
                Math.round(((double) matchedSymptomCount / totalDiseaseSymptoms) * 1000.0) / 10.0;
    }

    public Disease getDisease() {
        return disease;
    }

    public int getMatchedSymptomCount() {
        return matchedSymptomCount;
    }

    public int getTotalDiseaseSymptoms() {
        return totalDiseaseSymptoms;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }
}