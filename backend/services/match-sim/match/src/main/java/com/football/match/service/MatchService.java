package com.football.match.service;

import com.football.match.model.Match;
import com.football.match.model.MatchSummary;
import com.football.match.repository.MatchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Match createMatch(Match match){
        return matchRepository.save(match);
    }

    public Page<MatchSummary> listPageMatch(int page, int size) {
        return matchRepository.findAllProjectedBy(PageRequest.of(page, size));
    }

    public void deleteMatch(String matchId) {
        matchRepository.deleteById(matchId);
    }
}
