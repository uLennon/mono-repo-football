package com.football.result.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.football.result.model.ResultMatch;
import com.football.result.model.Team;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        record Message(String content) {}
        record Choice(Message message) {}
        record BaseResp(int status_code, String status_msg) {}
        record Response(List<Choice> choices, BaseResp base_resp) {}

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
        var response = restTemplate.postForEntity(API_URL, entity, Response.class).getBody();

        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            return gerarDescricaoEmpate(team1, team2);
        }

        return response.choices().getFirst().message().content();
    }

    private String gerarDescricaoEmpate(Team team1, Team team2) {
        try {
            var objectMapper = new ObjectMapper();

            var description = """
                    Jogo equilibrado! O %s pressionou mais e criou boas oportunidades, \
                    mas não conseguiu abrir o placar. O %s, com uma defesa sólida, \
                    teve dificuldades em reagir, mas segurou o empate. \
                    Resultado final: %s 0 x 0 %s.
                    """.formatted(team1.getName(), team2.getName(), team1.getName(), team2.getName());

            var result = Map.of(
                    "scoreteamHome", 0,
                    "scoreteamAway", 0,
                    "winner", "Empate"
            );

            var resultMatch = Map.of(
                    "matchDate", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    "result", result,
                    "teams", List.of(team1, team2),
                    "description", description
            );
            return objectMapper.writeValueAsString(resultMatch);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
