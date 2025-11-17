package com.football.team.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
    private Integer id;
    private String name;
    private String position;
    private Integer number;
    private Map<String, String> coords;

}
