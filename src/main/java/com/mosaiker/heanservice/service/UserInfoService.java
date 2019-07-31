package com.mosaiker.heanservice.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(value = "user-service")
@Service
public interface UserInfoService {
  @RequestMapping(method = RequestMethod.GET, value = "/getSimpleInfo")
  JSONObject getSimpleInfo(@RequestParam Long uId);
  @RequestMapping(method = RequestMethod.GET, value = "/getFollowings")
  JSONObject getFollowings(@RequestParam Long uId);
  @RequestMapping(method = RequestMethod.GET,value = "/getFollowers")
  JSONObject getFollowers(@RequestParam Long uId);

//  JSONObject getSimpleInfo(Long uId);
//  JSONObject getFollowings(Long uId);
//  JSONObject getFollowers(Long uId);
}
