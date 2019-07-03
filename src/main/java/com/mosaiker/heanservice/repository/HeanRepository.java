package com.mosaiker.heanservice.repository;

import com.mosaiker.heanservice.entity.Hean;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HeanRepository extends MongoRepository<Hean,String> {
  List<Hean> findAllByUId(Long uId);
  List<Hean> findAll();
  Boolean deleteByHId(String hId);
}
