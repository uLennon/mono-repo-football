package com.football.team.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "correlates")
@Data
public class Correlate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "coords_top")
    private String top;

    @Column(name = "coords_left")
    private String left;

    @Column()
    private Integer presetId;

    @Column(name = "team_Id")
    private Integer teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", insertable = false, updatable = false)
    @JsonBackReference("player-correlates")
    private Player player;

}
