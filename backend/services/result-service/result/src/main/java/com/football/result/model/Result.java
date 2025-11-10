package com.football.result.model;

public class Result {
    private Integer scoreteamHome;
    private Integer scoreteamAway;
    private String winner;

    public Integer getScoreteamHome() {
        return scoreteamHome;
    }

    public void setScoreteamHome(Integer scoreteamHome) {
        this.scoreteamHome = scoreteamHome;
    }

    public Integer getScoreteamAway() {
        return scoreteamAway;
    }

    public void setScoreteamAway(Integer scoreteamAway) {
        this.scoreteamAway = scoreteamAway;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "Result{" +
                "scoreteamHome=" + scoreteamHome +
                ", scoreteamAway=" + scoreteamAway +
                ", winner='" + winner + '\'' +
                '}';
    }
}
