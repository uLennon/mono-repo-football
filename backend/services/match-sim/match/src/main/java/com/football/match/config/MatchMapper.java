package com.football.match.config;

import com.football.match.model.Match;
import com.football.match.model.MatchDTO;
import com.football.match.model.PlayMatch;
import com.football.match.model.Team;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MatchMapper {
    public PlayMatch mapToPlayMatch(Match match1, MatchDTO matchDTO) {
        PlayMatch playMatch = new PlayMatch();

        playMatch.setMatchDate(match1.getMatchDate().toString());

        List<Team> teams = new ArrayList<>();

        Team team1 = new Team();
        team1.setTeamId(match1.getTeamId());
        team1.setName(match1.getName());
        team1.setPresetStrategy(match1.getPresetStrategy());
        team1.setPresetFormation(match1.getPresetFormation());
        teams.add(team1);

        Team team2 = new Team();
        team2.setTeamId(matchDTO.getTeamId());
        team2.setName(matchDTO.getName());
        team2.setPresetStrategy(matchDTO.getPresetStrategy());
        team2.setPresetFormation(matchDTO.getPresetFormation());
        teams.add(team2);

        playMatch.setTeams(teams);

        return playMatch;
    }
}
