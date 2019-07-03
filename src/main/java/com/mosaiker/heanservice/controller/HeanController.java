package com.mosaiker.heanservice.controller;


import com.mosaiker.heanservice.entity.Hean;
import com.mosaiker.heanservice.entity.Picture;
import com.mosaiker.heanservice.service.HeanService;
import com.mosaiker.heanservice.service.PictureService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

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
@RequestMapping(value = "/hean")
public class HeanController {

    @Autowired
    private PictureService pictureService;

    private static final String baseUrl = "http://localhost:7190/hean/pictures/get/";

    @Autowired
    private HeanService heanService;

    @RequestMapping(value = "/{uId}", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject findAllByUId(@PathVariable(value = "uId") Long uId) {
        List<Hean> heanList = heanService.findHeansByUId(uId);
        JSONObject result = new JSONObject();
        result.put("heanArray", heanList);
        result.put("message", "ok");
        return result;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject findAll() {
        List<Hean> heanList = heanService.findAllHeans();
        JSONObject result = new JSONObject();
        result.put("heanArray", heanList);
        result.put("message", "ok");
        return result;
    }

    @RequestMapping(value = "/delete/{hId}", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteHean(@PathVariable(value = "hId") String hId) {
        Boolean isdeleted = heanService.deleteByHId(hId);
        JSONObject result = new JSONObject();
        if (isdeleted) {
            result.put("message", "ok");
        } else {
            result.put("message", "not found");
        }
        return result;
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadPicture(@RequestParam(value = "pictures") MultipartFile[] files,
                                    @RequestParam(value = "uId") Long uId, @RequestParam(value = "text") String text,
                                    @RequestParam(value = "location") String location) {
        JSONObject result = new JSONObject();
        if ((files == null && text == null) || files.length <= 0) {
            result.put("message", "请发送非空信息");
            return result;
        }
        List<String> pUrls = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            try {
                String url = pictureService.uploadPicture(file, baseUrl);
                pUrls.add(url);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                result.put("message", "第" + i + "张图片上传失败");
                return result;
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
            result.put("message", "请提供正确的定位格式（经纬高分别以\',\'分离）");
            return result;
        }
    }

    @RequestMapping(value = "/pictures/get/{pId}", method = RequestMethod.GET, produces = {
            MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] getPicture(@PathVariable String pId) {
        byte[] data = null;
        Picture picture = pictureService.findPictureByPId(pId);
        if (picture != null) {
            data = picture.getContent().getData();
        } else {
            System.out.println("get picture fail");
        }
        return data;
    }
}