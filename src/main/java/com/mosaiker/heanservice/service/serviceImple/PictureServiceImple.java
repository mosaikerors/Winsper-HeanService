package com.mosaiker.heanservice.service.serviceImple;
import com.mosaiker.heanservice.entity.Picture;
import com.mosaiker.heanservice.repository.PictureRepository;
import com.mosaiker.heanservice.service.PictureService;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.util.List;


@Service

public class PictureServiceImple implements PictureService {

  @Autowired
  private PictureRepository pictureRepository;


  @Override
  public Picture findPictureByPId(String pId) {
    return pictureRepository.findPictureByPId(pId);
  }


  @Override
  public List<Picture> findPicturesByPIds(List<String> pIds) {
    return null;
  }


  @Override
  public String uploadPicture(MultipartFile file, String baseUrl) throws IOException {
    if (file.getContentType().equals("image/jpg") || file.getContentType().equals("image/png")
        || file.getContentType().equals("image/jpeg")) {
      Picture picture = new Picture();
      picture.setContent(new Binary(file.getBytes()));
      picture.setContentType(file.getContentType());
      picture.setSize(file.getSize());
      Picture savedFile = pictureRepository.save(picture);
      return baseUrl + savedFile.getPId();
    } else {
      System.out.println(file.getContentType());
      throw new IOException();
    }
  }
}
