package com.mosaiker.heanservice.controller;

import com.alibaba.fastjson.JSONObject;

import com.mosaiker.heanservice.entity.Hean;
import com.mosaiker.heanservice.entity.HeanComment;
import com.mosaiker.heanservice.service.HeanCommentService;
import com.mosaiker.heanservice.service.UserInfoService;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang.ObjectUtils.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/comment")
public class HeanCommentController {

  @Autowired
  private HeanCommentService heanCommentService;
  private UserInfoService userInfoService;
/*
  @RequestMapping(value = "/showByHId", method = RequestMethod.GET)
  @ResponseBody
  public JSONObject showByHId(@RequestParam(name = "hId") String hId) {
    JSONObject ret = new JSONObject();
    HeanComment heanComment = heanCommentService.findHeanCommentsOrNew(hId);
    List<Comment> comments = heanComment.getReplies();
    if (null != comments && comments.size() > 0) {
      ret.put("comments", comments);
      ret.put("message", "ok");
      return ret;
    } else {
      ret.put("message", "no comment.");
      return ret;
    }
  }
*/
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

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @ResponseBody
  public JSONObject add(@RequestBody JSONObject param) {
    try {
      JSONObject ret = new JSONObject();
      JSONObject com = new JSONObject();
      String hId = param.getString("hId");
      Long uId = param.getLong("uId");
      String targetCommentId = param.getString("targetCommentId");
      String content = param.getString("content");
      HeanComment newComment = new HeanComment(hId, uId, content, new Date().getTime(),
          targetCommentId, new ArrayList<String>());
      String result = heanCommentService.saveComment(newComment);
      com.put("commentId", newComment.getCommmentId());
      com.put("commenter", userInfoService.getSimpleInfo(uId).get("username"));
      if (targetCommentId != null) {
        Long commentedUId = heanCommentService.findHeanCommentByCId(targetCommentId)
            .getuId();
        com.put("commented", userInfoService.getSimpleInfo(commentedUId).get("username"));
      } else {
        com.put("commented", null);
      }
      com.put("time", newComment.getCommentTime());
      com.put("content", content);

      ret.put("message", result);
      ret.put("comment", com);
      return ret;

    } catch (Exception e) {
      JSONObject ret = new JSONObject();
      ret.put("message", "error");
      return ret;
    }
  }
}
