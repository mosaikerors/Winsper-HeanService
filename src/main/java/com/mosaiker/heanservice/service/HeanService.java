package com.mosaiker.heanservice.service;

import com.mosaiker.heanservice.entity.Hean;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface HeanService {
  List<Hean>  findHeansByUId(Long uId);
  //List<Hean> findHeansByTag(String tag);
  List<Hean>  findAllHeans();
  Boolean deleteByHId(String hId);
  Hean upload(Hean hean);
  List<Hean> findAllMarkedByUId(Long uId);
  Boolean toggleLike(String hId,Long uId);
  Boolean toggleStar(String hId,Long uId);
  Hean findHeanByHId(String hId);
}
