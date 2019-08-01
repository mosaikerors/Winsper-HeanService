package com.mosaiker.heanservice.repository;


import com.mosaiker.heanservice.entity.Marked;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkRepository extends MongoRepository<Marked, String> {

  Marked findByUId(Long uId);
}
