package com.mosaiker.heanservice.repository;


import com.mosaiker.heanservice.entity.Marked;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkRepository extends CrudRepository<Marked, Long> {

  Marked findByUId(Long uId);
}
