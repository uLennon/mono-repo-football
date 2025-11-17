package com.football.result.repository;

import com.football.result.model.ResultMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ResultMachRepository extends MongoRepository<ResultMatch,String> {

    Page<ResultMatch> findByTeamsName(String teamsName, Pageable pageable);
    List<ResultMatch> findByTeamsNameOrderByMatchDateDesc(String teamsName);

}
