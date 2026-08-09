package com.nadiya.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadiya.backend.entity.Symptom;
import com.nadiya.backend.service.SymptomService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin("*")
public class AiController {

    private final SymptomService symptomService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${GEMINI_API_KEY:}")
    private String geminiApiKey;

    public AiController(SymptomService symptomService) {
        this.symptomService = symptomService;
    }

    @PostMapping("/match-symptoms")
    public Map<String, Object> matchSymptoms(@RequestBody Map<String, String> request) {
        String userDescription = request.get("description");

        if (userDescription == null || userDescription.isBlank()) {
            return Map.of("matchedSymptoms", List.of(), "message", "Please describe how you're feeling.");
        }

        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            return Map.of("matchedSymptoms", List.of(), "message", "AI assistant is not configured.");
        }

        List<Symptom> allSymptoms = symptomService.getAllSymptoms();
        String symptomNames = allSymptoms.stream()
                .map(Symptom::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        String prompt = "You are a medical symptom matching assistant for a symptom checker app. "
                + "A user will describe how they feel in their own words, in plain language. "
                + "From this exact list of known symptom names, return ONLY the names that best match what the user described. "
                + "Respond with ONLY a JSON array of strings, using the EXACT names from the list, nothing else — no explanation, no markdown. "
                + "If nothing matches well, return an empty array []. "
                + "Known symptom list: [" + symptomNames + "]. "
                + "User description: \"" + userDescription.replace("\"", "'") + "\"";

        try {
            String responseText = callGemini(prompt);
            String cleaned = responseText.trim()
                    .replaceAll("^```json", "")
                    .replaceAll("^```", "")
                    .replaceAll("```$", "")
                    .trim();

            JsonNode namesArray = objectMapper.readTree(cleaned);
            List<Symptom> matched = new ArrayList<>();

            if (namesArray.isArray()) {
                for (JsonNode nameNode : namesArray) {
                    String name = nameNode.asText();
                    allSymptoms.stream()
                            .filter(s -> s.getName().equalsIgnoreCase(name))
                            .findFirst()
                            .ifPresent(matched::add);
                }
            }

            return Map.of(
                    "matchedSymptoms", matched,
                    "message", matched.isEmpty()
                            ? "Couldn't confidently match that to a known symptom. Try rephrasing, or browse the list directly."
                            : "Here's what matched what you described."
            );

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("matchedSymptoms", List.of(), "message", "AI assistant is temporarily unavailable. Please try again or select symptoms manually.");
        }
    }

    private String callGemini(String prompt) throws Exception {
        String requestBody = objectMapper.writeValueAsString(Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
        ));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent"))
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", geminiApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("GEMINI STATUS CODE: " + response.statusCode());
        System.out.println("GEMINI RAW RESPONSE: " + response.body());

        JsonNode root = objectMapper.readTree(response.body());
        return root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
    }
}