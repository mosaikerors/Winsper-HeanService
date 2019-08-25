package com.mosaiker.heanservice.repository;


import com.mosaiker.heanservice.entity.HeanComment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface HeanCommentRepository extends MongoRepository<HeanComment, String> {

  HeanComment findHeanCommentByHId(String hId);

  HeanComment findHeanCommentByCommentId(String CommentId);

  List<HeanComment> findAllByUId(Long uId);

}
