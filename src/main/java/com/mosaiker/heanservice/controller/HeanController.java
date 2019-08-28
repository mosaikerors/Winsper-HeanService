package com.mosaiker.heanservice.controller;

import com.alibaba.fastjson.JSONArray;
import com.mosaiker.heanservice.entity.Contribution;
import com.mosaiker.heanservice.entity.Hean;
import com.mosaiker.heanservice.entity.Picture;
import com.mosaiker.heanservice.repository.HeanRepository;
import com.mosaiker.heanservice.service.ContributionService;
import com.mosaiker.heanservice.service.HeanService;
import com.mosaiker.heanservice.service.PictureService;
import com.mosaiker.heanservice.service.UserInfoService;
import java.util.Calendar;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@RestController
public class HeanController {

    @Value("${picturesBaseUrl}")
    String baseUrl;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private HeanRepository heanRepository;
    @Autowired
    private HeanService heanService;
    @Autowired
    private ContributionService contributionService;

    @RequestMapping(value = "/point/all", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject findAllPoint(@RequestParam Double longitude, @RequestParam Double latitude,
                                   @RequestParam String follower, @RequestParam String time, @RequestHeader Long uId) {
        List<Hean> locList = heanService.findAllHeans(longitude, latitude);
        List<Hean> folList = locList;
        List<Hean> timeList = locList;
        if (!follower.equals("all")) {
            folList = findFollowerPoint(follower, uId);
        }
        if (!time.equals("all")) {
            timeList = findTimePoint(time, uId);
        }
        timeList.retainAll(locList);
        timeList.retainAll(folList);
        return points(timeList);
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

    private List<Hean> findFollowerPoint(String follower, Long uId) {
        List<Hean> heans = new ArrayList<>();
        if (follower.equals("me")) {
            List<Long> followings = userInfoService.getFollowings(uId).getObject("following", List.class);
            for (Long Id : followings) {
                List<Hean> list = heanService.findHeansByUId(Id);
                heans.removeAll(list);
                heans.addAll(list);
            }
        } else if (follower.equals("mutual")) {
            List<Long> followings = userInfoService.getFollowings(uId).getObject("following", List.class);
            List<Long> followers = userInfoService.getFollowers(uId).getObject("follower", List.class);
            followings.retainAll(followers);
            for (Long Id : followings) {
                List<Hean> list = heanService.findHeansByUId(Id);
                heans.removeAll(list);
                heans.addAll(list);
            }
        }
        return heans;
    }

    private List<Hean> findTimePoint(String time, Long uId) {
        Long boundary;
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        if (time.equals("day")) {//过去一天
            c.add(Calendar.DATE, -1);
        } else if (time.equals("week")) {//过去七天
            c.add(Calendar.DATE, -7);
        } else if (time.equals("month")) {//过去一月
            c.add(Calendar.MONTH, -1);
        } else if (time.equals("year")) {//过去一年
            c.add(Calendar.YEAR, -1);
        }
        boundary = c.getTime().getTime();
        return heanRepository.findAllByCreatedTimeGreaterThanEqual(boundary) == null ? new ArrayList<Hean>()
                : heanRepository.findAllByCreatedTimeGreaterThanEqual(boundary);
    }

    @RequestMapping(value = "/card", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject findOneCardHean(@RequestParam String hId, @RequestHeader("uId") Long uId) {
        Hean dest = heanService.findHeanByHId(hId);
        JSONObject result = new JSONObject(true);
        if (dest == null) {
            result.put("rescode", 3);
            return result;
        }
        if (dest.getUId().equals(uId) || userInfoService.getSimpleInfo(dest.getUId())
                .getBoolean("isHeanPublic")) {
            result.put("heanCard", dest.ToCard(uId));
            result.put("rescode", 0);
            return result;
        } else {
            result.put("rescode", 3);
            return result;
        }
    }


    @RequestMapping(value = "/detailed", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject findOneDetailedHean(@RequestParam String hId, @RequestHeader("uId") Long uId) {
        JSONObject result = new JSONObject();
        Hean dest = heanService.findHeanByHId(hId);
        if (dest == null) {
            result.put("rescode", 3);
            return result;
        }
        JSONObject destDetail = dest.ToDetail(uId);
        JSONObject info = userInfoService.getSimpleInfo(destDetail.getLong("uId"));
        destDetail.put("avatar", info.get("avatarUrl"));
        destDetail.put("username", info.get("username"));
        destDetail.put("comments", heanService.allComments(hId));
        result.put("rescode", 0);
        result.put("hean", destDetail);
        return result;
    }


    @RequestMapping(value = "/cardlist", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject findAllByUId(@RequestParam Long owner, @RequestHeader("uId") Long viewer) {
        Boolean isPublic = userInfoService.getSimpleInfo(owner).getBoolean("isHeanPublic");
        if (owner.equals(viewer) || isPublic) {
            List<Hean> heanList = heanService.findHeansByUId(owner);
            JSONObject result = new JSONObject(true);
            if (heanList != null) {
                JSONArray heanCards = new JSONArray();
                for (Hean hean : heanList) {
                    heanCards.add(hean.ToCard(viewer));
                }
                result.put("heanCards", heanCards);
                result.put("rescode", 0);
                return result;
            } else {
                result.put("rescode", 0);
                result.put("heanCards", new JSONArray());
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
    public JSONObject findStaredByUId(@RequestParam Long owner, @RequestHeader("uId") Long viewer) {
        Boolean isPublic = userInfoService.getSimpleInfo(owner).getBoolean("isCollectionPublic");
        if (owner.equals(viewer) || isPublic) {
            List<Hean> heanList = heanService.findAllMarkedByUId(owner);
            JSONObject result = new JSONObject(true);
            if (heanList != null) {
                JSONArray heanCards = new JSONArray();
                for (Hean hean : heanList) {
                    heanCards.add(hean.ToCard(viewer));
                }
                result.put("heanCards", heanCards);
                result.put("rescode", 0);
                return result;
            } else {
                result.put("rescode", 0);
                result.put("heanCards", new JSONArray());
                return result;
            }
        } else {
            JSONObject result = new JSONObject();
            result.put("rescode", 3);
            return result;
        }
    }


    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject deleteHean(@RequestParam String hId, @RequestHeader("uId") Long uId) {
        Hean gotH = heanService.findHeanByHId(hId);
        JSONObject result = new JSONObject();
        if (gotH.getUId().equals(uId)) {
            heanService.deleteByHId(hId);
            result.put("rescode", 0);
        } else {
            result.put("rescode", 3);
        }
        return result;
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public JSONObject uploadHean(@RequestParam(value = "pictures", required = false) MultipartFile[] files,
                                 @RequestHeader(value = "uId") Long uId, @RequestParam(value = "text", required = false) String text,
                                 @RequestParam(value = "location") String location) {
        JSONObject result = new JSONObject();
        if ((files == null || files.length <= 0) && (text == null || text.equals(""))) {
            result.put("rescode", 4);
            return result;
        } else if (files!=null&&files.length > 4) {
            result.put("rescode", 1);//图片数大于4
            return result;
        }
        String[] locations = location.split(",");//位置格式
        if (locations.length != 3) {
            result.put("rescode", 5);
            return result;
        }
        List<String> pUrls = new ArrayList<>();
        for (int i = 0; files!=null&&i < files.length; i++) {
            MultipartFile file = files[i];
            String url = pictureService.uploadPicture(file, baseUrl);
            if (url == null) {
                result.put("rescode", 3);
                result.put("badPicture", i);
                return result;
            }
            pUrls.add(url);
        }
        double longtitude = Double.parseDouble(locations[0]);
        double latitude = Double.parseDouble(locations[1]);
        double height = Double.parseDouble(locations[2]);
        Hean upload = new Hean(uId, new Date().getTime(), text, longtitude, latitude, height, pUrls);
        heanService.upload(upload);
        result.put("rescode", 0);
        return result;
    }


    @RequestMapping(value = "/pictures/get/{pId}", method = RequestMethod.GET, produces = {
            MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] getPicture(@PathVariable String pId) {
        Picture picture = pictureService.findPictureByPId(pId);
        byte[] data = picture.getContent().getData();

        return data;
    }


    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @ResponseBody//点赞函
    public JSONObject setLike(@RequestBody JSONObject param, @RequestHeader("uId") Long uId) {
        JSONObject result = new JSONObject();
        String hId = param.getString("hId");
        Boolean cur = heanService.setLike(hId, uId);
        result.put("rescode", 0);
        return result;
    }


    @RequestMapping(value = "/dislike", method = RequestMethod.POST)
    @ResponseBody//点赞函
    public JSONObject setUnLike(@RequestBody JSONObject param, @RequestHeader("uId") Long uId) {
        JSONObject result = new JSONObject();
        String hId = param.getString("hId");
        Boolean cur = heanService.setUnLike(hId, uId);
        result.put("rescode", 0);
        return result;
    }


    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    @ResponseBody//点赞函
    public JSONObject setCollect(@RequestBody JSONObject param, @RequestHeader("uId") Long uId) {
        JSONObject result = new JSONObject();
        String hId = param.getString("hId");
        Boolean cur = heanService.setStar(hId, uId);
        result.put("rescode", 0);
        return result;
    }


    @RequestMapping(value = "/uncollect", method = RequestMethod.POST)
    @ResponseBody//点赞函
    public JSONObject setUnCollect(@RequestBody JSONObject param, @RequestHeader("uId") Long uId) {
        JSONObject result = new JSONObject();
        String hId = param.getString("hId");
        Boolean cur = heanService.cancelStar(hId, uId);
        result.put("rescode", 0);
        return result;
    }


//    @RequestMapping(value = "/submission", method = RequestMethod.POST)
//    @ResponseBody
//    public JSONObject contributeNew(@RequestParam(value = "pictures") MultipartFile[] files,
//                                    @RequestParam(value = "uId") Long uId, @RequestParam(value = "text") String text,
//                                    @RequestParam(value = "location") String location) {
//        JSONObject uploadResult = new JSONObject();
//        uploadResult = uploadHean(files, uId, text, location);
//        if (uploadResult.getIntValue("rescode") != 0) {
//            return uploadResult;
//        }
//        return contributeByHId(uploadResult.getString("hId"));
//    }


    @RequestMapping(value = "/submission", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject contributeByHId(@RequestBody JSONObject param, @RequestHeader("uId") Long uId) {
        String hId = param.getString("hId");
        JSONObject result = new JSONObject();
        if (!heanRepository.findByHId(hId).getUId().equals(uId)) {
            result.put("rescode", 2);
            return result;
        }
        contributionService
            .addNewContribution(hId, uId, param.getString("reason"));
        result.put("rescode", 0);
        return result;
    }

    @RequestMapping(value = "/submission/selected", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject findContributionsByDate(@RequestParam(value = "date") Long date, @RequestHeader("uId") Long uId) {
        JSONObject result = new JSONObject();
        result.put("rescode", 0);
        JSONArray array = new JSONArray();
        List<Contribution> list =contributionService.findContributionsByDate(date);
        for(Contribution one:list){
            Hean hean = heanRepository.findByHId(one.getHId());
            array.add(hean.ToCard(uId));
        }
        result.put("heanCards", array);
        return result;
    }


    @RequestMapping(value = "/submission/list", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject findContributionsByUId(@RequestParam(value = "owner") Long owner, @RequestHeader("uId") Long uId) {
        JSONObject result = new JSONObject();
        result.put("rescode", 0);
        JSONArray array = new JSONArray();
        List<Contribution> list =contributionService.findContributionsByUId(owner);
        for(Contribution one:list){
            Hean hean = heanRepository.findByHId(one.getHId());
            if (hean == null) {
                continue;
            }
            array.add(hean.ToCard(uId));
        }
        result.put("heanCards", array);
        return result;
    }
}
