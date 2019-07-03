package com.mosaiker.heanservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.mosaiker.heanservice.entity.Picture;
import com.mosaiker.heanservice.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/hean")
public class HeanController {
    @Autowired
    private PictureService pictureService;

    private static final String baseUrl = "http://localhost:7190/hean/pictures/get/";

    @RequestMapping(value = "/pictures/upload", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadPicture(@RequestParam(value = "pictures") MultipartFile[] files){
        JSONObject result = new JSONObject();
        if (files == null || files.length <= 0) {
            result.put("message", "请选择一张图片");
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
        result.put("message", "ok");
        result.put("pictures", pUrls);
        return result;
    }

    @RequestMapping(value = "/pictures/get/{pId}", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] getPicture(@PathVariable String pId){
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
