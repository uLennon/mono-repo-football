package com.football.match.service;

import com.football.match.config.MatchMapper;
import com.football.match.model.Match;
import com.football.match.model.MatchDTO;
import com.football.match.model.MatchSummary;
import com.football.match.model.PlayMatch;
import com.football.match.repository.MatchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final KafkaTemplate<String, PlayMatch> kafkaTemplate;
    private final MatchMapper matchMapper;

    public MatchService(MatchRepository matchRepository, KafkaTemplate<String, PlayMatch> kafkaTemplate, MatchMapper matchMapper) {
        this.matchRepository = matchRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.matchMapper = matchMapper;
    }

    public Match createMatch(Match match){
        return matchRepository.save(match);
    }

    public Page<MatchSummary> listPageMatch(int page, int size) {
        return matchRepository.findAllProjectedBy(PageRequest.of(page, size));
    }

    public void deleteMatch(String matchId) {
        matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("Match not found"));
        matchRepository.deleteById(matchId);
    }

    public PlayMatch buildPlayMatch(MatchDTO matchDTO) {
        Match match = matchRepository.findById(matchDTO.getTeamIdVerso()).orElseThrow(() -> new RuntimeException("Team not found"));
        PlayMatch playMatch = matchMapper.mapToPlayMatch(match, matchDTO);
        kafkaTemplate.send("match-topic",playMatch);
        matchRepository.delete(match);
        return playMatch;
    }
}
