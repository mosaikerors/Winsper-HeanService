package com.mosaiker.heanservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.mosaiker.heanservice.entity.Comment;
import com.mosaiker.heanservice.entity.HeanComment;
import com.mosaiker.heanservice.service.HeanCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/comment")
public class HeanCommentController {
    @Autowired
    private HeanCommentService heanCommentService;

    @RequestMapping(value = "/showByHId",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject showByHId(@RequestParam(name = "hId") String hId){
        JSONObject ret = new JSONObject();
        HeanComment heanComment = heanCommentService.findHeanCommentsOrNew(hId);
        List<Comment> comments = heanComment.getReplies();
        if(null != comments&&comments.size()>0) {
            ret.put("comments",comments);
            ret.put("message", "ok");
            return ret;
        } else {
            ret.put("message","no comment.");
            return ret;
        }
    }

    /*@RequestMapping(value = "/showByUId",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject showByUId(@RequestParam(name = "uId") Long uId){
        JSONObject ret = new JSONObject();
        HeanComment heanComment = heanCommentService.findHeanCommentsOrNew(uId);
        List<Comment> comments = heanComment.getReplies();
        if(null != comments&&comments.size()>0) {
            ret.put("comments",comments);
            ret.put("message", "ok");
            return ret;
        } else {
            ret.put("message","no comment.");
            return ret;
        }
    }*/

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject add(@RequestBody JSONObject param){
        JSONObject ret = new JSONObject();
        String hId = param.getString("hId");
        Long uId = param.getLong("uId");
        List<Integer> commentIndex = param.getJSONArray("index").toJavaList(Integer.class);
        String username = param.getString("username");
        String content = param.getString("content");
        String result = heanCommentService.saveReply(hId, commentIndex, uId, username, content);
        ret.put("message",result);
        return ret;
    }
}
