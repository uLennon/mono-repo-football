package com.football.result.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.football.result.model.ResultMatch;
import com.football.result.model.Team;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IaGenerativeService {
    @Value("${myConfig.API_URL}")
    private String API_URL;
    @Value("${myConfig.API_KEY}")
    private String API_KEY;
    @Value("${myConfig.prompt}")
    private String promptt;

    private final RestTemplate restTemplate;
    public IaGenerativeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }

    public String generativeMatchWithIA(Team team1, Team team2) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        headers.set("Content-Type", "application/json");
        String prompt = String.format(promptt,
                team1.getName(), team1.getPresetFormation(), team1.getPresetStrategy(), team2.getName(), team2.getPresetFormation(), team2.getPresetStrategy()
        );

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "MiniMax-M2");
        requestBody.put("messages", List.of(
                Map.of("role", "user",
                        "content", prompt)
        ));
        requestBody.put("temperature",1.0);
        requestBody.put("max_tokens", 10000);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, entity, Map.class);

        return filterResponse(response.getBody());
    }

    private String filterResponse(Map<String, Object> responseBody) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            return "Error response: " + e.getMessage();
        }
    }

    public ResultMatch buildResultMatch(Team team1, Team team2) {
        var description = generativeMatchWithIA(team1, team2);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(description, ResultMatch.class);
        }catch (Exception e) {
           throw new RuntimeException();
        }
    }
}
