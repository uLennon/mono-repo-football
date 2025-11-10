package com.football.team.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "presets")
public class Preset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer identifier;
    private Integer presetId;

    private String presetStrategy;
    private String presetFormation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    @JsonBackReference("team-presets")
    private Team team;

    @OneToMany(mappedBy = "preset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("preset-players")
    private List<Player> presetPlayers;

}