package com.football.team.service;

import com.football.team.model.Team;
import com.football.team.repository.TeamRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team findByName(String name) {
        return teamRepository.findByName(name);
    }
}
