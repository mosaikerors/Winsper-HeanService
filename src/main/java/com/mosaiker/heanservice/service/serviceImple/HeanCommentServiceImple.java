package com.mosaiker.heanservice.service.serviceImple;

//import com.mosaiker.heanservice.entity.Comment;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mosaiker.heanservice.entity.Hean;
import com.mosaiker.heanservice.entity.HeanComment;
import com.mosaiker.heanservice.repository.HeanCommentRepository;
import com.mosaiker.heanservice.repository.HeanRepository;
import com.mosaiker.heanservice.service.HeanCommentService;
import com.mosaiker.heanservice.service.HeanService;
import com.mosaiker.heanservice.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class HeanCommentServiceImple implements HeanCommentService {

    @Autowired
    private HeanCommentRepository heanCommentRepository;
    @Autowired
    private HeanRepository heanRepository;

    private UserInfoService userInfoService;
    @Override
    public HeanComment findHeanCommentByCId(String cId) {
        HeanComment commented = heanCommentRepository.findHeanCommentByCommmentId(cId);
        return commented;
    }/*

    @Override
    public List<Comment> findComments(String hId) {
        HeanComment bookComment = findHeanCommentsOrNew(hId);
        return bookComment.getReplies();
    }

    //一级评论
    @Override
    public String saveHeanComments(String hId, List<Comment> comments) {
        HeanComment bc = findHeanCommentsOrNew(hId);
        bc.setReplies(comments);
        heanCommentRepository.save(bc);
        return "ok";
    }*/

    //给一个HeanComment加一个子Comment
    @Override
    public Integer saveComment(HeanComment newComment) {
        heanCommentRepository.save(newComment);
        if(newComment.getTargetCommentId()!=null) {
            HeanComment commented = heanCommentRepository
                .findHeanCommentByCommmentId(newComment.getTargetCommentId());
            commented.addReply(newComment.getCommmentId());
            heanCommentRepository.save(commented);
        }
        Hean comed=heanRepository.findByHId(newComment.getHId());
        comed.addComment(newComment.getHId());
        heanRepository.save(comed);

        return 0;
    }
    @Override
    public JSONObject getComJSONObject(String cId){
        HeanComment heanComment = findHeanCommentByCId(cId);
        JSONObject ret = new JSONObject();
        ret.put("commentId",heanComment.getCommmentId());
        String commenter = userInfoService.getSimpleInfo(heanComment.getuId()).getString("username");
        ret.put("commenter",commenter);
        if(heanComment.getTargetCommentId()!=""&&heanComment.getTargetCommentId()!=null){
            HeanComment target = findHeanCommentByCId(heanComment.getTargetCommentId());
            String commented =userInfoService.getSimpleInfo(target.getuId()).getString("username");
            ret.put("commented",commented);
        }
        ret.put("time",heanComment.getCommentTime());
        ret.put("content",heanComment.getContent());
        return ret;
    }
    @Override
    public JSONArray findAllByUId(Long owner){
        List<HeanComment> commentList = heanCommentRepository.findAllByUId(owner);
        JSONArray array = new JSONArray();
        for(HeanComment com :commentList){
            JSONObject onecom = new JSONObject();
            Boolean isCom = ((com.getTargetCommentId()==null)||(com.getTargetCommentId().isEmpty()));
            onecom.put("isComment",isCom);
            if(isCom){
                onecom.put("username",heanRepository.findByHId(com.getHId()));
            }else{
                Long uId = heanCommentRepository.findHeanCommentByCommmentId(com.getTargetCommentId()).getuId();
                String username = userInfoService.getSimpleInfo(uId).getString("username");
                onecom.put("username",username);
            }
            onecom.put("content",com.getContent());
            onecom.put("time",com.getCommentTime());
            onecom.put("hId",com.getHId());
            array.add(onecom);
        }
        return array;
    }

}
