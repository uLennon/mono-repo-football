package com.football.match.service;

import com.football.match.config.MatchMapper;
import com.football.match.model.Match;
import com.football.match.model.MatchDTO;
import com.football.match.model.MatchSummary;
import com.football.match.model.PlayMatch;
import com.football.match.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private KafkaTemplate<String, PlayMatch> kafkaTemplate;

    @Mock
    private MatchMapper matchMapper;

    @InjectMocks
    private MatchService matchService;

    @Captor
    private ArgumentCaptor<PlayMatch> playMatchCaptor;


    @Test
    void testCreateMatch() {

        Match match = new Match();
        match.setId("123");

        when(matchRepository.save(match)).thenReturn(match);

        Match result = matchService.createMatch(match);

        assert result.getId().equals("123");

        verify(matchRepository, times(1)).save(match);
    }

    @Test
    void testGetAllMatchesReturnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);  // Example Pageable
        Page<MatchSummary> emptyPage = new PageImpl<>(List.of());  // Empty page

        when(matchRepository.findAllProjectedBy(pageable)).thenReturn(emptyPage);

        Page<MatchSummary> result = matchService.listPageMatch(0,10);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteMatchSuccess() {
        Match match = new Match();
        match.setId("ABC");

        when(matchRepository.findById("ABC")).thenReturn(Optional.of(match));

        matchService.deleteMatch("ABC");

        verify(matchRepository).findById("ABC");
        verify(matchRepository).deleteById("ABC");
    }


    @Test
    void testDeleteMatchNotFound() {

        when(matchRepository.findById("999")).thenReturn(Optional.empty());

        try {
            matchService.deleteMatch("999");
            assert false : "Exception expected";
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Match not found");
        }

        verify(matchRepository).findById("999");
        verify(matchRepository, never()).deleteById(anyString());
    }

    @Test
    void testBuildPlayMatchSuccess() {

        MatchDTO matchDTO = new MatchDTO();
        matchDTO.setTeamIdVerso("123");

        Match match = new Match();
        match.setId("123");

        PlayMatch playMatch = new PlayMatch();

        when(matchRepository.findById("123")).thenReturn(Optional.of(match));
        when(matchMapper.mapToPlayMatch(match, matchDTO)).thenReturn(playMatch);

        PlayMatch result = matchService.buildPlayMatch(matchDTO);

        assert result == playMatch;

        verify(matchRepository).findById("123");

        verify(matchMapper).mapToPlayMatch(match, matchDTO);

        verify(kafkaTemplate).send(eq("match-topic"), playMatchCaptor.capture());
        PlayMatch captured = playMatchCaptor.getValue();
        assert captured == playMatch;
        verify(matchRepository).delete(match);
    }

    @Test
    void testBuildPlayMatchNotFound() {
        MatchDTO dto = new MatchDTO();
        dto.setTeamIdVerso("777");
        when(matchRepository.findById("777")).thenReturn(Optional.empty());

        try {
            matchService.buildPlayMatch(dto);
            assert false : "Exception expected";
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Team not found");
        }

        verify(matchRepository).findById("777");
        verify(kafkaTemplate, never()).send(any(), any());
        verify(matchRepository, never()).delete(any());
    }
}