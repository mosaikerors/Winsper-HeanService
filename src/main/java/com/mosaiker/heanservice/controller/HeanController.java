package com.mosaiker.heanservice.controller;

import static java.lang.Math.abs;

import com.alibaba.fastjson.JSONArray;
import com.mosaiker.heanservice.entity.Hean;
import com.mosaiker.heanservice.entity.Picture;
import com.mosaiker.heanservice.service.HeanCommentService;
import com.mosaiker.heanservice.service.HeanService;
import com.mosaiker.heanservice.service.PictureService;
import com.mosaiker.heanservice.service.UserInfoService;
import com.mosaiker.heanservice.utils.Geohash;
import com.netflix.discovery.converters.Auto;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class HeanController {

  @Autowired
  private PictureService pictureService;
  @Autowired
  private HeanCommentService heanCommentService;
  @Autowired
  private UserInfoService userInfoService;
  @Value("${picturesBaseUrl}")
  String baseUrl;

  @Autowired
  private HeanService heanService;

  @RequestMapping(value = "/point/all", method = RequestMethod.GET)
  @ResponseBody
  public JSONObject findAllPoint(@RequestParam Double lon,@RequestParam Double lat) {
    List<Hean> heanList = heanService.findAllHeans(lon,lat);
    return points(heanList);
  }

  private JSONObject points(List<Hean> heanList) {
    JSONObject result = new JSONObject(true);
    JSONArray pointArray = new JSONArray();
    for (Hean hean : heanList) {
      pointArray.add(hean.ToJSONPoint());
    }
    result.put("heans", pointArray);
    result.put("rescode", 0);
    return result;
  }

  @RequestMapping(value = "/point/follower", method = RequestMethod.GET)
  @ResponseBody
  public JSONObject findFollowerPoint(@RequestParam Long uId) {
    List<Long> followings = userInfoService.getFollowings(uId).getObject("following", List.class);
    List<Hean> heans = new ArrayList<>();
    for (Long Id : followings) {
      List<Hean> list = heanService.findHeansByUId(Id);
      heans.removeAll(list);
      heans.addAll(list);
    }
    return points(heans);
  }

  @RequestMapping(value = "/point/mutualFollow", method = RequestMethod.GET)
  @ResponseBody
  public JSONObject findMutualFollowPoint(@RequestParam Long uId) {
    List<Long> followings = userInfoService.getFollowings(uId).getObject("following", List.class);
    List<Long> followers = userInfoService.getFollowers(uId).getObject("follower",List.class);
    followings.retainAll(followers);
    List<Hean> heans = new ArrayList<>();
    for (Long Id : followings) {
      List<Hean> list = heanService.findHeansByUId(Id);
      heans.removeAll(list);
      heans.addAll(list);
    }
    return points(heans);
  }
  @RequestMapping(value = "/card", method = RequestMethod.GET)
  @ResponseBody
  public JSONObject findOneCardHean(@RequestParam String hId, @RequestParam Long uId) {
    Hean dest = heanService.findHeanByHId(hId);
    JSONObject result = new JSONObject(true);

    result.put("heanCard", dest.ToCard(uId));
    result.put("rescode", 0);
    return result;
  }


  @RequestMapping(value = "/detailed", method = RequestMethod.GET)
  @ResponseBody
  public JSONObject findOneDetailedHean(@RequestParam String hId, @RequestParam Long uId,@RequestParam Double lon,@RequestParam Double lat) {
    JSONObject info = userInfoService.getSimpleInfo(uId);
    Hean dest = heanService.findHeanByHId(hId);
    JSONObject destDetail = dest.ToDetail(uId);
    if(abs(new Geohash().encode(lon, lat)-dest.getGeoStr())<=4) {
      destDetail.put("avatar", info.get("avatarUrl"));
      destDetail.put("username", info.get("username"));
      JSONObject result = new JSONObject(true);

      result.put("rescode", 0);
      result.put("hean", destDetail);
      result.put("comments", heanService.allComments(hId));

      return result;
    }else{
      destDetail.put("rescode",3);//位置不够近
      return destDetail;
    }
  }


  @RequestMapping(value = "/cardlist", method = RequestMethod.GET)
  @ResponseBody
  public JSONObject findAllByUId(@RequestParam Long owner, @RequestParam Long viewer) {
    Boolean isPublic = userInfoService.getSimpleInfo(owner).getBoolean("isHeanPublic");
    if (owner.equals(viewer) || isPublic) {
      List<Hean> heanList = heanService.findHeansByUId(owner);
      if (heanList != null) {
        JSONObject result = new JSONObject(true);
        JSONArray heanCards = new JSONArray();
        for (Hean hean : heanList) {
          heanCards.add(hean.ToCard(viewer));
        }
        result.put("heanCards", heanCards);
        result.put("rescode", 0);
        return result;
      } else {
        JSONObject result = new JSONObject();
        result.put("rescode", 4);
        return result;
      }
    } else {
      JSONObject result = new JSONObject();
      result.put("rescode", 3);
      return result;
    }
  }


  @RequestMapping(value = "/collection", method = RequestMethod.GET)
  @ResponseBody
  public JSONObject findStaredByUId(@RequestParam Long owner, @RequestParam Long viewer) {
    Boolean isPublic = userInfoService.getSimpleInfo(owner).getBoolean("isCollectionPublic");
    if (owner.equals(viewer) || isPublic) {
      List<Hean> heanList = heanService.findHeansByUId(owner);
      if (heanList != null) {
        JSONObject result = new JSONObject(true);
        JSONArray heanCards = new JSONArray();
        for (Hean hean : heanList) {
          heanCards.add(hean.ToCard(viewer));
        }
        result.put("heanCards", heanCards);
        result.put("rescode", 0);
        return result;
      } else {
        JSONObject result = new JSONObject();
        result.put("rescode", 4);
        return result;
      }
    } else {
      JSONObject result = new JSONObject();
      result.put("rescode", 3);
      return result;
    }
  }


//  @RequestMapping(value = "/all", method = RequestMethod.GET)
//  @ResponseBody
//  public JSONObject findAll() {
//    List<Hean> heanList = heanService.findAllHeans();
//    JSONObject result = new JSONObject(true);
//    JSONArray heanArray = new JSONArray();
//    for (Hean hean : heanList) {
//      heanArray.add(hean.ToJSONObject());
//    }
//    result.put("heanArray", heanArray);
//    result.put("rescode", 0);
//    return result;
//  }

  @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
  @ResponseBody
  public JSONObject deleteHean(@RequestParam String hId, @RequestParam Long uId) {
    Hean gotH = heanService.findHeanByHId(hId);
    JSONObject result = new JSONObject();
    if (gotH.getUId().equals(uId)) {
      Boolean isDeleted = heanService.deleteByHId(hId);
      if (isDeleted) {
        result.put("rescode", 0);
      } else {
        result.put("rescode", 1);//该函不存在
      }
    } else {
      result.put("rescode", 3);
    }
    return result;
  }


  @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  @ResponseBody
  public JSONObject uploadHean(@RequestParam(value = "pictures") MultipartFile[] files,
      @RequestParam(value = "uId") Long uId, @RequestParam(value = "text") String text,
      @RequestParam(value = "location") String location) {
    JSONObject result = new JSONObject();
    if ((files == null || files.length <= 0) && (text == null || text.equals(""))) {
      result.put("rescode", 4);
      return result;
    } else if (files.length > 4) {
      result.put("rescode", 1);//图片数大于4
      return result;
    }
    List<String> pUrls = new ArrayList<>();
    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        MultipartFile file = files[i];
        try {
          String url = pictureService.uploadPicture(file, baseUrl);
          pUrls.add(url);
        } catch (Exception e) {
          System.out.println(e.getMessage());
          result.put("rescode", 3);
          result.put("badPicture", i);
          return result;
        }
      }
    }
    String[] locations = location.split(",");
    if (locations.length == 3) {
      double longtitude = Double.parseDouble(locations[0]);
      double latitude = Double.parseDouble(locations[1]);
      double height = Double.parseDouble(locations[2]);
      Hean upload = new Hean(uId, new Date(), text, longtitude, latitude, height, pUrls);
      heanService.upload(upload);
      result.put("rescode", 0);
      return result;
    } else {
      result.put("rescode", 5);
      return result;
    }
  }

  @RequestMapping(value = "/pictures/get/{pId}", method = RequestMethod.GET, produces = {
      MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
  @ResponseBody
  public byte[] getPicture(@PathVariable String pId) {
    Picture picture = pictureService.findPictureByPId(pId);
    byte[] data = picture.getContent().getData();

    return data;
  }


  @RequestMapping(value = "/toggleLike", method = RequestMethod.POST)
  @ResponseBody//点赞或取消点赞函
  public JSONObject toggleLike(@RequestBody JSONObject param) {
    JSONObject result = new JSONObject();
    Long uId = param.getLong("uId");
    String hId = param.getString("hId");
    Boolean cur = heanService.toggleLike(hId, uId);
    result.put("rescode", 0);
    result.put("status", cur == Boolean.FALSE ? "canceled" : "added");
    return result;
  }


  @RequestMapping(value = "/toggleStar", method = RequestMethod.POST)
  @ResponseBody//收藏或取消收藏函
  public JSONObject toggleStar(@RequestBody JSONObject param) {
    JSONObject result = new JSONObject();
    Long uId = param.getLong("uId");
    String hId = param.getString("hId");
    Boolean cur = heanService.toggleStar(hId, uId);
    result.put("message", "ok");
    result.put("status", cur == Boolean.FALSE ? "canceled" : "added");
    return result;

  }

}
