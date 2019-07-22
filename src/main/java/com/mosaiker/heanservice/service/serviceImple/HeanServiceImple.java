package com.mosaiker.heanservice.service.serviceImple;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mosaiker.heanservice.entity.Hean;
import com.mosaiker.heanservice.repository.HeanRepository;
import com.mosaiker.heanservice.repository.MarkRepository;
import com.mosaiker.heanservice.service.HeanCommentService;
import com.mosaiker.heanservice.service.HeanService;
import com.mosaiker.heanservice.utils.Geohash;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeanServiceImple implements HeanService {

  @Autowired
  private HeanRepository heanRepository;
  @Autowired
  private MarkRepository markRepository;
  @Autowired
  private HeanCommentService heanCommentService;
  public List<Hean> findHeansByUId(Long uId) {
    return heanRepository.findAllByUId(uId).isEmpty()?null:heanRepository.findAllByUId(uId);
  }

  public List<Hean> findAllHeans(Double lon,Double lat) {
    Long loc = new Geohash().encode(lon,lat);
    return heanRepository.findAllByGeoStrBetween(loc-250*250,loc+250*250);
  }

  public Boolean deleteByHId(String hId) {
    return heanRepository.deleteByHId(hId);
  }

  public Hean upload(Hean hean){
    return heanRepository.save(hean);
  }
  public List<Hean> findAllMarkedByUId(Long uId){
    List<String> starList = markRepository.findByUId(uId).getMarks();
    List<Hean> heanList = new ArrayList<>();
    for(String hId:starList){
      heanList.add(heanRepository.findByHId(hId));
    }
    return heanList;
  }
  public Boolean toggleLike(String hId,Long uId){
    List<Long> likelist = heanRepository.findLikeUIdsByHId(hId);
    if(likelist.contains(uId)) {
      likelist.remove(uId);
      heanRepository.findByHId(hId).setLikeUIds(likelist);
      return Boolean.FALSE;
    }
    else {
      likelist.add(uId);
      heanRepository.findByHId(hId).setLikeUIds(likelist);
      return Boolean.TRUE;
    }
  }
  public Boolean toggleStar(String hId,Long uId){
    List<Long> starlist = heanRepository.findStarUIdsByHId(hId);
    List<String> mystar = markRepository.findByUId(uId).getMarks();
    if(starlist.contains(uId)) {
      mystar.remove(hId);
      starlist.remove(uId);
      heanRepository.findByHId(hId).setStarUIds(starlist);
      markRepository.findByUId(uId).setMarks(mystar);
      return Boolean.FALSE;
    }
    else {
      mystar.add(hId);
      starlist.add(uId);
      heanRepository.findByHId(hId).setStarUIds(starlist);
      markRepository.findByUId(uId).setMarks(mystar);
      return Boolean.TRUE;
    }
  }
  public Hean findHeanByHId(String hId){
    return heanRepository.findByHId(hId);
  }

  public JSONArray allComments(String hId){
    Hean hean = heanRepository.findByHId(hId);
    JSONArray coms = new JSONArray();
    for(String cId:hean.getCommentIds()){
      coms.add(heanCommentService.getComJSONObject(cId));
    }
    return coms;
  }
}
