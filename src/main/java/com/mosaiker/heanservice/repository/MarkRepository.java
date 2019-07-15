package com.mosaiker.heanservice.repository;



import com.mosaiker.heanservice.entity.Marked;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MarkRepository extends MongoRepository<Marked,String> {
    Marked findByUId(Long uId);
}
