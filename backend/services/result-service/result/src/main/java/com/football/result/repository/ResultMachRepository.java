package com.football.result.repository;

import com.football.result.model.ResultMatch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultMachRepository extends MongoRepository<ResultMatch,String> {

}
