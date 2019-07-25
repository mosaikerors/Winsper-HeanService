package com.mosaiker.heanservice.service.serviceImple;

//import com.mosaiker.heanservice.entity.Comment;
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
/*
    //任意一级的回复
    //有点难啊，要搜索树，级联保存
    @Override
    public String saveReply(String hId, List<Integer> commentIndex, Long uId, String username, String content) {
        //初始化curComment和curCommentLayer
        HeanComment bookComment = findHeanCommentsOrNew(hId);
        Comment curComment = null;
        List<Comment> curLayerComments = bookComment.getReplies();
        if (commentIndex.size() == 0) {
            saveComment(bookComment,uId,username,content);
        } else {
            //通过commentIndex找到最底层的curComment，新增评论
            for (int i = 0; i < commentIndex.size(); i++) {
                if (curLayerComments != null) {
                    curComment = curLayerComments.get(commentIndex.get(i));
                    curLayerComments = curComment.getReplies();
                } else {
                    return "error";
                }
            }
            saveComment(curComment,uId,username,content);
        }
        heanCommentRepository.save(bookComment);
        return "ok";
    }*/
}
