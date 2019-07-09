package com.mosaiker.heanservice.repository;

import com.mosaiker.heanservice.entity.Comment;
import com.mosaiker.heanservice.entity.HeanComment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface HeanCommentRepository  extends MongoRepository<HeanComment, String> {
    Optional<HeanComment> findHeanCommentByHId(String hId);

}
