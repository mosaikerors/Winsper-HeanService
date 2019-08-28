package com.mosaiker.heanservice.service.serviceImple;

import com.alibaba.fastjson.JSONArray;
import com.mosaiker.heanservice.entity.Hean;
import com.mosaiker.heanservice.entity.Marked;
import com.mosaiker.heanservice.repository.HeanRepository;
import com.mosaiker.heanservice.repository.MarkRepository;
import com.mosaiker.heanservice.service.HeanCommentService;
import com.mosaiker.heanservice.service.HeanService;
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
    return heanRepository.findAllByUId(uId);
  }

  public List<Hean> findAllHeans(Double lon, Double lat) {
    List<Hean> heans1 = heanRepository.findAllByLatitudeBetween(lat - 0.005, lat + 0.005);
    List<Hean> heans2 = heanRepository.findAllByLongtitudeBetween(lon - 0.005, lon + 0.005);
    if (heans1 == null || heans2 == null) {
      return new ArrayList<>();
    }
    heans1.retainAll(heans2);
    return heans1;
  }

  public void deleteByHId(String hId) {
    heanRepository.deleteByHId(hId);
  }

  public Hean upload(Hean hean) {
    return heanRepository.save(hean);
  }

  public List<Hean> findAllMarkedByUId(Long uId) {
    Marked marked = markRepository.findByUId(uId);
    if (marked == null) {
      return null;
    }
    List<String> starList = marked.getMarks();
    List<Hean> heanList = new ArrayList<>();
    for (String hId : starList) {
      heanList.add(heanRepository.findByHId(hId));
    }
    return heanList;
  }

  public Boolean setLike(String hId, Long uId) {
    List<Long> likelist = findLikeUIdsByHId(hId);
    if (!likelist.contains(uId)) {
      likelist.add(uId);
      Hean hean = heanRepository.findByHId(hId);
      hean.setLikeUIds(likelist);
      heanRepository.save(hean);
    }
    return Boolean.TRUE;
  }

  public Boolean setUnLike(String hId, Long uId) {
    List<Long> likelist = findLikeUIdsByHId(hId);
    if (likelist.contains(uId)) {
      likelist.remove(uId);
      Hean hean = heanRepository.findByHId(hId);
      hean.setLikeUIds(likelist);
      heanRepository.save(hean);
    }
    return Boolean.TRUE;
  }

  public Boolean setStar(String hId, Long uId) {
    List<Long> starlist = findStarUIdsByHId(hId);
    Marked marked = markRepository.findByUId(uId);
    if (marked == null) {
      marked = new Marked(uId);
    }
    List<String> mystar = marked.getMarks();
    if (!starlist.contains(uId)) {
      mystar.add(hId);
      starlist.add(uId);
      Hean hean = heanRepository.findByHId(hId);
      hean.setStarUIds(starlist);
      marked.setMarks(mystar);
      heanRepository.save(hean);
      markRepository.save(marked);
    }
    return Boolean.TRUE;
  }

  public Boolean cancelStar(String hId, Long uId) {
    List<Long> starlist = findStarUIdsByHId(hId);
    Marked marked = markRepository.findByUId(uId);
    if (marked == null) {
      return Boolean.FALSE;
    }
    List<String> mystar = marked.getMarks();
    if (starlist.contains(uId)) {
      mystar.remove(hId);
      starlist.remove(uId);
      Hean hean = heanRepository.findByHId(hId);
      hean.setStarUIds(starlist);
      marked.setMarks(mystar);
      heanRepository.save(hean);
      markRepository.save(marked);
    }
    return Boolean.FALSE;
  }

  public Hean findHeanByHId(String hId) {
    return heanRepository.findByHId(hId);
  }

  public JSONArray allComments(String hId) {
    Hean hean = heanRepository.findByHId(hId);
    JSONArray coms = new JSONArray();
    for (String cId : hean.getCommentIds()) {
      coms.add(heanCommentService.getComJSONObject(cId));
    }
    return coms;
  }

  @Override
  public List<Long> findLikeUIdsByHId(String hId) {
    return heanRepository.findByHId(hId).getLikeUIds();
  }

  @Override
  public List<Long> findStarUIdsByHId(String hId) {
    return heanRepository.findByHId(hId).getStarUIds();
  }
}
