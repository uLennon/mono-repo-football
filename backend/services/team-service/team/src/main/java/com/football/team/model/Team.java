package com.football.team.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "teams")
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("team-presets")
    private List<Preset> presets;

}
