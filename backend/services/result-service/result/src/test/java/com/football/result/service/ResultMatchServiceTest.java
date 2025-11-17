package com.football.result.service;

import com.football.result.model.PlayMatch;
import com.football.result.model.ResultMatch;
import com.football.result.model.Team;
import com.football.result.repository.ResultMachRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultMatchServiceTest {

    @Mock
    private IaGenerativeService iaGenerativeService;

    @Mock
    private ResultMachRepository resultMachRepository;

    @InjectMocks
    private ResultMatchService resultMatchService;

    @Captor
    private ArgumentCaptor<ResultMatch> matchCaptor;

    @Test
    void testCreatResultMatch() {
        Team team1 = new Team();
        team1.setName("Team1");

        Team team2 = new Team();
        team2.setName("Team2");

        PlayMatch playMatch = new PlayMatch();
        playMatch.setMatchDate("dd/MM/yyyy");
        playMatch.setTeams(List.of(team1, team2));

        ResultMatch generated = new ResultMatch();
        generated.setDescription("Match Finish");

        when(iaGenerativeService.buildResultMatch(team1, team2))
                .thenReturn(generated);

        resultMatchService.creatResultMatch(playMatch);

        verify(iaGenerativeService, times(1))
                .buildResultMatch(team1, team2);

        verify(resultMachRepository, times(1))
                .save(matchCaptor.capture());

        ResultMatch saved = matchCaptor.getValue();
        assert saved.getTeams().size() == 2;
        assert saved.getDescription().equals("Match Finish");
        assert saved.getMatchDate().equals(playMatch.getMatchDate());
    }

}
