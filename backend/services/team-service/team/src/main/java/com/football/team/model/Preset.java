package com.football.team.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "presets")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Preset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer identifier;
    private Integer presetId;
    private String presetStrategy;
    private String presetFormation;

    @OneToMany(mappedBy = "preset", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Placement> playerPlacements = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "team_id")
    @JsonBackReference
    private Team team;

    @OneToMany(mappedBy = "preset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("preset-players")
    private List<Player> presetPlayers;

}