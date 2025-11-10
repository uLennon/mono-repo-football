package com.football.team.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="UsersTeam")
public class UserTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "team_name", nullable = false)
    private String teamName;
    @Column(name = "account", nullable = false)
    private String account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
}
