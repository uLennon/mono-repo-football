package com.football.result.model;

import java.util.List;

public class PlayMatch {
    private String matchDate;

    private List<Team> teams;

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        return "PlayMatch{" +
                "matchDate='" + matchDate + '\'' +
                ", teams=" + teams +
                '}';
    }
}
