package com.mosaiker.heanservice.service;

import com.mosaiker.heanservice.entity.Comment;
import com.mosaiker.heanservice.entity.HeanComment;

import java.util.List;

public interface HeanCommentService {
    public HeanComment findHeanCommentsOrNew(String hId);

    public List<Comment> findComments(String hId);

    public String saveHeanComments(String hId, List<Comment> comments);

    public void saveComment(HeanComment oldComment, Long uId, String username, String content);

    public String saveReply(String hId, List<Integer> commentIndex, Long uId, String username, String content);
}

