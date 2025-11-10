package com.football.result.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "resultMatchs")
public class ResultMatch {
    @Id
    private String id;
    private String matchDate;
    private Result result;
    private List<Team> teams;
    private String Description;


    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        return "ResultMatch{" +
                "id='" + id + '\'' +
                ", matchDate='" + matchDate + '\'' +
                ", result=" + result +
                ", teams=" + teams +
                ", Description='" + Description + '\'' +
                '}';
    }
}
