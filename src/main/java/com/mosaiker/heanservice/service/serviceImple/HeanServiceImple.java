package com.mosaiker.heanservice.service.serviceImple;

import com.mosaiker.heanservice.entity.Hean;
import com.mosaiker.heanservice.repository.HeanRepository;
import com.mosaiker.heanservice.service.HeanService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeanServiceImple implements HeanService {

  @Autowired
  private HeanRepository heanRepository;

  public List<Hean> findHeansByUId(Long uId) {
    return heanRepository.findAllByUId(uId).isEmpty()?null:heanRepository.findAllByUId(uId);
  }

  public List<Hean> findAllHeans() {
    return heanRepository.findAll();
  }

  public Boolean deleteByHId(String hId) {
    return heanRepository.deleteByHId(hId);
  }

  public Hean upload(Hean hean){
    return heanRepository.save(hean);
  }
}
