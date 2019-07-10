package com.mosaiker.heanservice.controller;

import com.alibaba.fastjson.JSONArray;
import com.mosaiker.heanservice.entity.Hean;
import com.mosaiker.heanservice.entity.Picture;
import com.mosaiker.heanservice.service.HeanService;
import com.mosaiker.heanservice.service.PictureService;
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

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class HeanController {

    @Autowired
    private PictureService pictureService;

    @Value("${picturesBaseUrl}")
    String baseUrl;

    @Autowired
    private HeanService heanService;

    @RequestMapping(value = "/byUId", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject findAllByUId(@RequestBody JSONObject param) {
        Long uId = param.getLong("uId");
        List<Hean> heanList = heanService.findHeansByUId(uId);
        if(heanList!=null) {
            JSONObject result = new JSONObject(1000000,true);
            JSONArray heanArray1 = new JSONArray();
            for(Hean hean:heanList){
                heanArray1.add(hean.ToJSONObject());
            }
            result.put("heanArray", heanList);
            result.put("message", "ok");
            return result;
        }else{
            JSONObject result = new JSONObject();
            result.put("message","not found");
            return result;
        }
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject findAll() {
        List<Hean> heanList = heanService.findAllHeans();
        JSONObject result = new JSONObject(1000000,true);
        JSONArray heanArray = new JSONArray();
        for(Hean hean:heanList){
            heanArray.add(hean.ToJSONObject());
        }
        result.put("heanArray", heanArray);
        result.put("message", "ok");
        return result;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject deleteHean(@RequestBody JSONObject param) {
        String hId = param.getString("hId");
        Boolean isDeleted = heanService.deleteByHId(hId);
        JSONObject result = new JSONObject();
        if (isDeleted) {
            result.put("message", "ok");
        } else {
            result.put("message", "not found");
        }
        return result;
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST,produces="application/json; charset=UTF-8")
    @ResponseBody
    public JSONObject uploadHean(@RequestParam(value = "pictures") MultipartFile[] files,
                                    @RequestParam(value = "uId") Long uId, @RequestParam(value = "text") String text,
                                    @RequestParam(value = "location") String location) {
        JSONObject result = new JSONObject();
        if ((files == null||files.length<=0) && (text == null||text.equals(""))) {
            result.put("message", "pics and text cannot all be null");
            return result;
        }
        List<String> pUrls = new ArrayList<>();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                try {
                    String url = pictureService.uploadPicture(file, baseUrl);
                    pUrls.add(url);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    result.put("message", "No." + i + " pic fail");
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
            result.put("message", "ok");
            result.put("pictures", pUrls);
            return result;
        } else {
            result.put("message", "wrong location format");
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
}
