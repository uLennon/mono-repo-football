package com.football.team.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "placements")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Placement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "coords_top")
    private String top;

    @Column(name = "coords_left")
    private String left;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "preset_id")
    @JsonBackReference
    private Preset preset;
}
