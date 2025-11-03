package com.football.match.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document(collection = "matches")
public class Match {
    @Id
    private String id;
    private Date matchDate;
    private int teamId;
    private String name;
    private String presetStrategy;
    private String presetFormation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPresetStrategy() {
        return presetStrategy;
    }

    public void setPresetStrategy(String presetStrategy) {
        this.presetStrategy = presetStrategy;
    }

    public String getPresetFormation() {
        return presetFormation;
    }

    public void setPresetFormation(String presetFormation) {
        this.presetFormation = presetFormation;
    }
}
