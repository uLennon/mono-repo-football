package com.football.result.model;

public class Team {
    private Integer teamId;
    private String name;
    private String presetStrategy;
    private String presetFormation;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
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

    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", name='" + name + '\'' +
                ", presetStrategy='" + presetStrategy + '\'' +
                ", presetFormation='" + presetFormation + '\'' +
                '}';
    }
}
