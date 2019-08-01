package com.mosaiker.heanservice.service;

import com.mosaiker.heanservice.entity.Picture;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public interface PictureService {

  Picture findPictureByPId(String pId);

  List<Picture> findPicturesByPIds(List<String> pIds);

  String uploadPicture(MultipartFile file, String baseUrl);

}





