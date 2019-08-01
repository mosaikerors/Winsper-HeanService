package com.mosaiker.heanservice.repository;

import com.mosaiker.heanservice.entity.Picture;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends MongoRepository<Picture, String> {

  Picture findPictureByPId(String pId);

}
