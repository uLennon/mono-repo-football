package com.football.match.repository;

import com.football.match.model.Match;
import com.football.match.model.MatchSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends MongoRepository<Match, String> {
    @Query(value = "{}", fields = "{ '_id':  1,'teamId': 1, 'name': 1 }")
    Page<MatchSummary> findAllProjectedBy(Pageable pageable);
}
