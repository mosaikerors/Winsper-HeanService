package com.mosaiker.heanservice.service.serviceImple;

import com.alibaba.fastjson.JSONObject;
import com.mosaiker.heanservice.service.UserInfoService;

public class UserInfoServiceImple implements UserInfoService {
    @Override
    public JSONObject getSimpleInfo(Long uId) {
        JSONObject result = new JSONObject();
        result.put("rescode", 0);
        result.put("avatarUrl", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1840528989,320128963&fm=26&gp=0.jpg");
        result.put("username", "czn");
        result.put("isHeanPublic", 1);
        result.put("isCollectionPublic", 1);
        return result;
    }

    @Override
    public JSONObject getFollowings(Long uId) {
        return null;
    }

    @Override
    public JSONObject getFollowers(Long uId) {
        return null;
    }
}
