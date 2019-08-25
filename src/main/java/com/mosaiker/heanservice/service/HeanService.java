package com.mosaiker.heanservice.service;

import com.alibaba.fastjson.JSONArray;
import com.mosaiker.heanservice.entity.Hean;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface HeanService {

  List<Hean> findHeansByUId(Long uId);

  List<Hean> findAllHeans(Double lon, Double lat);

  void deleteByHId(String hId);

  Hean upload(Hean hean);

  List<Hean> findAllMarkedByUId(Long uId);

  Boolean setLike(String hId, Long uId);

  Boolean setUnLike(String hId, Long uId);

  Boolean setStar(String hId, Long uId);

  Boolean cancelStar(String hId, Long uId);

  Hean findHeanByHId(String hId);

  JSONArray allComments(String hId);
}
