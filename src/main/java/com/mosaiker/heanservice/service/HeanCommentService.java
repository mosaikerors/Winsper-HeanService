package com.mosaiker.heanservice.service;

//import com.mosaiker.heanservice.entity.Comment;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mosaiker.heanservice.entity.HeanComment;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface HeanCommentService {
    HeanComment findHeanCommentByCId(String cId);

    Integer saveComment(HeanComment newComment);

    JSONObject getComJSONObject(String cId);

    JSONArray findAllByUId(Long owner);
}

