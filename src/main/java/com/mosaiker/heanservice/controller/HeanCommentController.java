package com.mosaiker.heanservice.controller;

import com.alibaba.fastjson.JSONObject;

import com.mosaiker.heanservice.entity.HeanComment;
import com.mosaiker.heanservice.service.HeanCommentService;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/comment")
public class HeanCommentController {

  @Autowired
  private HeanCommentService heanCommentService;


  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @ResponseBody
  public JSONObject add(@RequestBody JSONObject param, @RequestHeader("uId") Long uId) {
    try {
      JSONObject ret = new JSONObject();
      JSONObject com = new JSONObject();
      String hId = param.getString("hId");
      String targetCommentId = param.getString("targetCommentId");
      String content = param.getString("content");
      if (content.length() <= 0) {
        throw new Exception();
      }
      HeanComment newComment = new HeanComment(hId, uId, content, new Date().getTime(),
          targetCommentId, new ArrayList<String>());
      Integer result = heanCommentService.saveComment(newComment);
      ret.put("rescode", result);
      return ret;

    } catch (Exception e) {
      JSONObject ret = new JSONObject();
      ret.put("rescode", 3);
      return ret;
    }
  }

  @RequestMapping(value = "/", method = RequestMethod.POST)
  @ResponseBody
  public JSONObject getAllComms(@RequestHeader("uId") Long uId) {
    JSONObject ret = new JSONObject();
    ret.put("rescode", 0);
    ret.put("comments", heanCommentService.findAllByUId(uId));
    return ret;
  }
}
