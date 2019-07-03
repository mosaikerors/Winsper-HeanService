package com.mosaiker.heanservice.service;

import org.springframework.stereotype.Service;
import com.mosaiker.heanservice.entity.Picture;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public interface PictureService {
  Picture findPictureByPId(String pId);


  List<Picture> findPicturesByPIds(List<String> pIds);


  String uploadPicture(MultipartFile file, String baseUrl) throws IOException;
}





